package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.cashback.CashbackManager;
import org.poo.cashback.NrOfTransactionsCashback;
import org.poo.cashback.SpendingThresholdCashback;
import org.poo.commerciants.Commerciant;
import org.poo.commerciants.CommerciantRegistry;
import org.poo.discounts.DiscountStrategy;
import org.poo.discounts.DiscountStrategyFactory;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.UpgradePlanTransaction;
import org.poo.user.UserRegistry;
import org.poo.transaction.Transaction;
import org.poo.transaction.InsufficientFunds;
import org.poo.transaction.SendMoneyTransaction;
import org.poo.user.User;

/**
 * This class represents a command that sends money from one account to another.
 */
public final class SendMoneyCommand implements Command {
    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private double amount;
    private final String description;
    private final String email;
    private final String giverIBAN;
    private final String receiverIBAN;
    private final ExchangeRates exchangeRates;
    private final CommerciantRegistry commerciantRegistry;

    /**
     * Constructor for the SendMoneyCommand class.
     *
     * @param userRegistry  the user registry
     * @param output        the output array
     * @param timestamp     the timestamp of the transaction
     * @param giver         the IBAN of the giver
     * @param receiver      the IBAN of the receiver
     * @param amount        the amount of money to send
     * @param email         the email of the user
     * @param description   the description of the transaction
     * @param exchangeRates the exchange rates
     */
    public SendMoneyCommand(final UserRegistry userRegistry,
                            final ArrayNode output,
                            final int timestamp,
                            final String giver,
                            final String receiver,
                            final double amount,
                            final String email,
                            final String description,
                            final ExchangeRates exchangeRates,
                            final CommerciantRegistry commerciantRegistry) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.amount = amount;
        this.description = description;
        this.email = email;
        this.giverIBAN = giver;
        this.receiverIBAN = receiver;
        this.exchangeRates = exchangeRates;
        this.commerciantRegistry = commerciantRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {

        // find the user that sends the money using the email
        User giver = userRegistry.getUserByEmail(email);
        if (giver == null) {
            return;
        }

        // find the account of the giver using the IBAN
        Account giverAccount = giver.getAccountByIBAN(giverIBAN);
        if (giverAccount == null) {
            return;
        }

        // find the account of the receiver using the IBAN, then the alias
        Account receiverAccount = userRegistry.getAccountByIBAN(receiverIBAN);
        Commerciant commerciantReceiver = commerciantRegistry.getCommerciantByIBAN(receiverIBAN);
        if (receiverAccount == null) {
            receiverAccount = userRegistry.getAccountByAlias(receiverIBAN);
        }
        if (receiverAccount == null && commerciantReceiver == null) {
            ObjectNode objectNode = output.addObject();
            objectNode.put("command", "sendMoney");
            ObjectNode outputNode = objectNode.putObject("output");
            outputNode.put("description", "User not found");
            outputNode.put("timestamp", timestamp);
            objectNode.put("timestamp", timestamp);
            return;
        }

        User receiver = userRegistry.getUserByIBAN(receiverIBAN);
        if (receiver == null) {
            receiver = userRegistry.getUserByAlias(receiverIBAN);
        }

        // check if the user sends money to another user
        if (receiver != null) {

            // save the currency of the giver and the receiver
            String currencyFrom = giverAccount.getCurrency();
            String currencyTo = receiverAccount.getCurrency();

            // check if the currency of the giver is the same as the currency of the receiver
            if (currencyFrom.equals(currencyTo)) {
                // add the commission to the amount for the giver
                double amountWithCommission = giver.addCommission(amount, exchangeRates, currencyFrom);
                if (giverAccount.getBalance() < amountWithCommission) {
                    // if the giver does not have enough money,
                    // create a transaction with the message "Insufficient funds"
                    Transaction transaction = new InsufficientFunds(timestamp,
                            "Insufficient funds");
                    giver.addTransaction(transaction);
                    giverAccount.addTransaction(transaction);
                    return;
                }

                // update the balance of the giver and the receiver
                giverAccount.setBalance(giverAccount.getBalance() - amountWithCommission);
                receiverAccount.setBalance(receiverAccount.getBalance() + amount);

                // create a transaction for the giver and the receiver
                Transaction transaction = new SendMoneyTransaction(timestamp,
                        description, giverIBAN, receiverIBAN, amount,
                        currencyFrom, "sent");
                Transaction receiverTransaction = new SendMoneyTransaction(timestamp,
                        description, giverIBAN, receiverIBAN, amount,
                        currencyTo, "received");
                giver.addTransaction(transaction);
                receiver.addTransaction(receiverTransaction);

                giverAccount.addTransaction(transaction);
                receiverAccount.addTransaction(receiverTransaction);
                return;
            }

            // calculate the amount to transfer using the exchange rate
            double exchangeRate = exchangeRates.convertExchangeRate(currencyFrom, currencyTo);
            double amountToTransfer = amount * exchangeRate;

            double amountWithCommission = giver.addCommission(amount, exchangeRates, currencyFrom);

            // check if the giver has enough money
            if (giverAccount.getBalance() < amountWithCommission) {
                // if the giver does not have enough money,
                // create a transaction with the message "Insufficient funds"
                Transaction transaction = new InsufficientFunds(timestamp,
                        "Insufficient funds");
                giver.addTransaction(transaction);
                giverAccount.addTransaction(transaction);
                return;
            }

            // update the balance of the giver and the receiver
            giverAccount.setBalance(giverAccount.getBalance() - amountWithCommission);
            receiverAccount.setBalance(receiverAccount.getBalance() + amountToTransfer);

            // create a transaction for the giver and the receiver
            Transaction transaction = new SendMoneyTransaction(timestamp,
                    description, giverIBAN, receiverIBAN, amount, currencyFrom,
                    "sent");
            Transaction receiverTransaction = new SendMoneyTransaction(timestamp,
                    description, giverIBAN, receiverIBAN, amountToTransfer,
                    currencyTo, "received");
            giver.addTransaction(transaction);
            receiver.addTransaction(receiverTransaction);
            giverAccount.addTransaction(transaction);
            receiverAccount.addTransaction(receiverTransaction);

        }
        else {
            // the user sends money to a commerciant
            // the user will send money in his currency

            // add the commission to the amount for the giver
            String currencyFrom = giverAccount.getCurrency();
            double amountWithCommission = giver.addCommission(amount, exchangeRates, currencyFrom);
            if (giverAccount.getBalance() < amountWithCommission) {
                // if the giver does not have enough money,
                // create a transaction with the message "Insufficient funds"
                Transaction transaction = new InsufficientFunds(timestamp,
                        "Insufficient funds");
                giver.addTransaction(transaction);
                giverAccount.addTransaction(transaction);
                return;
            }

            // update the balance of the giver and the receiver
            giverAccount.setBalance(giverAccount.getBalance() - amountWithCommission);


            // create a transaction for the giver and the receiver
            Transaction transaction = new SendMoneyTransaction(timestamp,
                    description, giverIBAN, receiverIBAN, amount,
                    currencyFrom, "sent");
            giver.addTransaction(transaction);
            giverAccount.addTransaction(transaction);


            double rateForRon = exchangeRates.convertExchangeRate(currencyFrom, "RON");
            double amountRon = amount * rateForRon;

            String commerciant = commerciantReceiver.getCommerciant();

            Commerciant newCommerciant = commerciantRegistry.getCommerciantByName(commerciant);
            giverAccount.addCommerciant(newCommerciant);

            Commerciant existingCommerciant = giverAccount.getCommerciantByCommerciantName(commerciant);



            existingCommerciant.addAmountSpent(amount);
            // check if the account can receive cashback

            // now check if i had any discounts to apply to receive cashback and apply them
            // Apply discounts based on commerciant type
            DiscountStrategy commerciantStrategy = DiscountStrategyFactory.getStrategy(existingCommerciant.getType());
            if (commerciantStrategy != null) {
                commerciantStrategy.applyDiscount(giverAccount, existingCommerciant, amount);
            }

            // use the strategy pattern to apply the cashback
            // i saved the discounts in the account with this strategy
            CashbackManager cashbackManager = new CashbackManager();

            if (existingCommerciant.getCashbackStrategy().equals("spendingThreshold")) {
                giverAccount.addAmountSpentOnSTCommerciants(amountRon);
                cashbackManager.setStrategy(new SpendingThresholdCashback(giver.getServicePlan()));
            } else if (existingCommerciant.getCashbackStrategy().equals("nrOfTransactions")) {
                cashbackManager.setStrategy(new NrOfTransactionsCashback());
            }

            cashbackManager.applyCashback(existingCommerciant, giverAccount, amount, currencyFrom, exchangeRates);


            // Apply the spending threshold discount
            DiscountStrategy spendingThresholdStrategy = DiscountStrategyFactory.getStrategy("SpendingThreshold");
            if (spendingThresholdStrategy != null) {
                spendingThresholdStrategy.applyDiscount(giverAccount, existingCommerciant, amount);
            }

            if (amountRon > 300 && giver.getServicePlan().equals("silver")) {
                giver.incrementPaymentsOverThreeHundred();
                if (giver.getPaymentsOverThreeHundred() == 5) {
                    giver.setServicePlan("gold");
                    Transaction transaction1 = new UpgradePlanTransaction(timestamp,
                            "Upgrade plan", "gold", giverAccount.getIBAN());
                    giver.addTransaction(transaction1);
                }
            }


        }
    }
}
