package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.ClassicAccount;
import org.poo.cashback.CashbackManager;
import org.poo.cashback.NrOfTransactionsCashback;
import org.poo.cashback.SpendingThresholdCashback;
import org.poo.commerciants.CommerciantRegistry;
import org.poo.discounts.Discount;
import org.poo.discounts.DiscountStrategy;
import org.poo.discounts.DiscountStrategyFactory;
import org.poo.report.BusinessCommerciantReport;
import org.poo.report.CommerciantBusiness;
import org.poo.transaction.*;
import org.poo.user.UserRegistry;
import org.poo.commerciants.Commerciant;
import org.poo.report.ClassicReport;
import org.poo.report.PaymentsRecord;
import org.poo.user.User;
import org.poo.card.Card;
import org.poo.exchangeRates.ExchangeRates;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utils.Utils;

/**
 * Command class for the payOnline command.
 * Is is used to pay online using a card, if the card has enough funds.
 */
public final class PayOnlineCommand implements Command {
    private final UserRegistry userRegistry;
    final ExchangeRates exchangeRates;
    private final ArrayNode output;
    private final int timestamp;
    private final String cardNumber;
    private double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;
    private static final int MIN_DIFFERENCE = 30;
    private final CommerciantRegistry commerciantRegistry;

    /**
     * Constructor for the PayOnlineCommand class.
     *
     * @param userRegistry  the UserRegistry object
     * @param exchangeRates the ExchangeRates object
     * @param output        the ArrayNode object
     * @param timestamp     the timestamp
     * @param cardNumber    the card number
     * @param amount        the amount to pay
     * @param currency      the currency
     * @param description   the description
     * @param commerciant   the commerciant
     * @param email         the email
     */
    public PayOnlineCommand(final UserRegistry userRegistry,
                            final ExchangeRates exchangeRates,
                            final ArrayNode output,
                            final int timestamp,
                            final String cardNumber,
                            final double amount, final String currency,
                            final String description, final String commerciant,
                            final String email,
                            final CommerciantRegistry commerciantRegistry) {
        this.userRegistry = userRegistry;
        this.exchangeRates = exchangeRates;
        this.output = output;
        this.timestamp = timestamp;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
        this.commerciantRegistry = commerciantRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            return;
        }

        Card card = userRegistry.getCardByNumber(cardNumber);
        if (card == null) {
            ObjectNode outputNode = output.addObject();
            outputNode.put("command", "payOnline");
            ObjectNode outputObject = outputNode.putObject("output");
            outputObject.put("description", "Card not found");
            outputObject.put("timestamp", timestamp);
            outputNode.put("timestamp", timestamp);
            return;
        }

        Account account = userRegistry.getAccountByCardNumber(cardNumber);

        User businessUser = userRegistry.getUserByEmail(email);

        // check if it may be a business account
        if (!card.getOwnerEmail().equals(email)) {
            // if it is not a business account, than it is a classic or savings account
            // and it must belong to the user, so the email is wrong - error
            if (!account.getType().equals("business")) {
                ObjectNode outputNode = output.addObject();
                outputNode.put("command", "payOnline");
                ObjectNode outputObject = outputNode.putObject("output");
                outputObject.put("description", "Card not found");
                outputObject.put("timestamp", timestamp);
                outputNode.put("timestamp", timestamp);
                return;
            }
            else {
                // check if the user is the owner of the business account
                BusinessAccount businessAccount = (BusinessAccount) account;

                // check if the user is associated with the business account
                if (!businessAccount.isEmployee(businessUser) && !businessAccount.isManager(businessUser) && !businessAccount.getOwner().equals(businessUser)) {
                    ObjectNode outputNode = output.addObject();
                    outputNode.put("command", "payOnline");
                    ObjectNode outputObject = outputNode.putObject("output");
                    outputObject.put("description", "Card not found");
                    outputObject.put("timestamp", timestamp);
                    outputNode.put("timestamp", timestamp);
                    return;
                }

                User owner = businessAccount.getOwner();
                user = owner;
            }
        }

        String cardCurrency = account.getCurrency();



        // check if the card currency is different from the payment currency
        if (!cardCurrency.equals(currency)) {
            double rate = exchangeRates.convertExchangeRate(cardCurrency, currency);
            // check if the exchange rate was found
            if (rate != 0) {
                // calculate the amount in the card currency
                amount = amount / rate;
            }
        }

//        // check if the card is frozen
//        if (card.getStatus().equals("frozen")) {
//            // create a transaction for the frozen card
//            // the card cannot be used to make payments
//            Transaction transaction = new FrozenCard(timestamp,
//                    "The card is frozen");
//            user.addTransaction(transaction);
//            return;
//        }

        double amountToPay = user.addCommission(amount, exchangeRates, cardCurrency);
        System.out.println("amount to pay: " + amountToPay + " " + businessUser.getEmail() + " " + commerciant);

        // check if the account has enough funds
        if (account.getBalance() >= amountToPay) {

            // freeze the card if the balance is less than the minimum balance
//            if (account.getBalance() - amountToPay <= account.getMinBalance()) {
//                card.setStatus("frozen");
//
//                // create a transaction to warn the user that the card will be frozen
//                Transaction transactionErorr = new WarningForPay(timestamp,
//                        "You have reached the minimum amount "
//                                + "of funds, the card will be frozen");
//                user.addTransaction(transactionErorr);
//                return;
//            }

            // make the card status "warning" if the balance is less than the
            // minimum balance + 30
//                    if (account.getBalance() - account.getMinBalance() - amountToPay < MIN_DIFFERENCE) {
//
//                        // create a transaction to warn the user that the card will be frozen
//                        Transaction transactionErorr = new WarningForPay(timestamp,
//                                "You have reached the minimum amount "
//                                        + "of funds, the card will be frozen");
//                        user.addTransaction(transactionErorr);
//                        card.setStatus("warning");
//
//                    }

            // if the cases above are not true, the payment can be made
           // double roundedAmount = Math.round((account.getBalance() - amountToPay) * 100.0) / 100.0;


            if (account.getType().equals("business")) {
                BusinessAccount businessAccount = (BusinessAccount) account;

                if (!businessAccount.getOwner().equals(businessUser) && amount != 0) {
                    System.out.println(commerciant + " " + businessUser.getEmail());
                    System.out.println("timestamp: " + timestamp + " amount: " + amount);
                    // get the commerciant report for the business account
                    BusinessCommerciantReport businessCommerciantReport = businessAccount.getBusinessCommerciantReport();



                    if (businessAccount.isManager(businessUser)) {
                        // add the commerciant to the report
                        System.out.println("manager" + businessUser.getEmail());
                        CommerciantBusiness commerciantForReport = businessCommerciantReport.getCommerciantBusiness(commerciant);
                        if (commerciantForReport == null) {
                            commerciantForReport = new CommerciantBusiness(commerciant);
                            businessCommerciantReport.addCommerciantBusiness(commerciantForReport);
                        }

                        CommerciantBusiness commerciantBusiness = businessCommerciantReport.getCommerciantBusiness(commerciant);
                        businessAccount.addManagerSpentAmount(businessUser, amount);
                        businessAccount.setTotalSpent(businessAccount.getTotalSpent() + amount);
                        commerciantBusiness.addManager(businessUser);
                        commerciantBusiness.addAmountSpent(amount);

                    } else if (businessAccount.isEmployee(businessUser)) {
                        System.out.println("employee" + businessUser.getEmail() );
                        if (amountToPay > businessAccount.getMaxSpendLimit()) {
                            return;
                        }
                        // add the commerciant to the report
                        CommerciantBusiness commerciantForReport = businessCommerciantReport.getCommerciantBusiness(commerciant);
                        if (commerciantForReport == null) {
                            commerciantForReport = new CommerciantBusiness(commerciant);
                            businessCommerciantReport.addCommerciantBusiness(commerciantForReport);
                        }

                        CommerciantBusiness commerciantBusiness = businessCommerciantReport.getCommerciantBusiness(commerciant);
                        businessAccount.addEmployeeSpentAmount(businessUser, amount);
                        businessAccount.setTotalSpent(businessAccount.getTotalSpent() + amount);
                        commerciantBusiness.addEmployee(businessUser);
                        commerciantBusiness.addAmountSpent(amount);
                    }
                }
            }

            account.setBalance(account.getBalance() - amountToPay);


            System.out.println("balance " + account.getBalance());

            Transaction transaction = new CardPaymentTransaction(timestamp,
                    "Card payment", amount, commerciant);

            // create a transaction for the payment
            if (amount != 0) {
                user.addTransaction(transaction);
            }

            double rateForRon = exchangeRates.convertExchangeRate(cardCurrency, "RON");
            double amountRon = amountToPay * rateForRon;


            Commerciant newCommerciant = commerciantRegistry.getCommerciantByName(commerciant);
            account.addCommerciant(newCommerciant);

            Commerciant existingCommerciant = account.getCommerciantByCommerciantName(commerciant);

            System.out.println(commerciant + " " + account.getIBAN());


//            System.out.println(user.getEmail() + " " + existingCommerciant.getCommerciant() + " " + existingCommerciant.getNrOfTransactions()
//                    + " " + amountRon + " " + amountToPay + " " + currency + " "
//                    + timestamp);
            // check if the account can receive cashback

            // now check if i had any discounts to apply to receive cashback and apply them
            // Apply discounts based on commerciant type
            DiscountStrategy commerciantStrategy = DiscountStrategyFactory.getStrategy(existingCommerciant.getType());
            if (commerciantStrategy != null) {
                commerciantStrategy.applyDiscount(account, existingCommerciant, amount);
            }

            existingCommerciant.addAmountSpent(amount);

            System.out.println("amount spent: " + amountRon);

            // use the strategy pattern to apply the cashback
            // i saved the discounts in the account with this strategy
            CashbackManager cashbackManager = new CashbackManager();

            if (existingCommerciant.getCashbackStrategy().equals("spendingThreshold")) {
                account.addAmountSpentOnSTCommerciants(amountRon);
                cashbackManager.setStrategy(new SpendingThresholdCashback(user.getServicePlan()));
            } else if (existingCommerciant.getCashbackStrategy().equals("nrOfTransactions")) {
                cashbackManager.setStrategy(new NrOfTransactionsCashback());
            }

            cashbackManager.applyCashback(existingCommerciant, account, amount, cardCurrency, exchangeRates);

            // Apply the spending threshold discount
            DiscountStrategy spendingThresholdStrategy = DiscountStrategyFactory.getStrategy("SpendingThreshold");
            if (spendingThresholdStrategy != null) {
                spendingThresholdStrategy.applyDiscount(account, existingCommerciant, amount);
            }

            if (amountRon > 300 && user.getServicePlan().equals("silver")) {
                user.incrementPaymentsOverThreeHundred();
                if (user.getPaymentsOverThreeHundred() == 5) {
                    user.setServicePlan("gold");
                    Transaction transaction1 = new UpgradePlanTransaction(timestamp,
                            "Upgrade plan", "gold", account.getIBAN());
                    user.addTransaction(transaction1);
                }
            }


            // verify if the account is a classic account
            // in order to save the transaction for the report and the spending report
            if (account.getType().equals("classic")) {
                // save the commerciant in the account

                // add the transaction to the report and the payments record
                // (for the spending report)
                ClassicAccount classicAccount = (ClassicAccount) account;
                ClassicReport report = classicAccount.getReport();
                PaymentsRecord paymentsRecord = classicAccount.getPaymentsRecord();

                report.addTransaction(transaction);
                paymentsRecord.addTransaction(transaction);

                Commerciant commerciantForSpendingReport = new Commerciant(commerciant, amount, timestamp);
                classicAccount.addCommerciantForSpendingReport(commerciantForSpendingReport);

                //classicAccount.addCommerciantForSpendingReport(existingCommerciant);

            }

            // check if the card is a oneTimePay card
            if (card.getType().equals("oneTimePay")) {
                // the card will be used only once, then it will be destroyed
                // and a new card will be created

                // generate a new card number
                String newCardNumber = Utils.generateCardNumber();
                card.setCardNumber(newCardNumber);

                // create a transaction for the destroyed card and the new card
                Transaction transactionCardDestroyed = new CardDestroyed(timestamp,
                        "The card has been destroyed", account.getIBAN(),
                        cardNumber, user.getEmail());
                user.addTransaction(transactionCardDestroyed);

                Transaction transactionNewCard = new NewCardCreatedTransaction(timestamp,
                        "New card created", account.getIBAN(),
                        newCardNumber, user.getEmail());
                user.addTransaction(transactionNewCard);
            }

        } else {
            // if the payment cannot be made, create a transaction for insufficient funds
            Transaction transaction = new InsufficientFunds(timestamp,
                    "Insufficient funds");
            user.addTransaction(transaction);
        }



    }
}
