package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.account.ClassicAccount;
import org.poo.cashback.CashbackManager;
import org.poo.cashback.NrOfTransactionsCashback;
import org.poo.cashback.SpendingThresholdCashback;
import org.poo.commerciants.CommerciantRegistry;
import org.poo.discounts.DiscountStrategy;
import org.poo.discounts.DiscountStrategyFactory;
import org.poo.report.BusinessCommerciantReport;
import org.poo.report.CommerciantBusiness;
import org.poo.transaction.CardDestroyed;
import org.poo.transaction.CardPaymentTransaction;
import org.poo.transaction.InsufficientFunds;
import org.poo.transaction.NewCardCreatedTransaction;
import org.poo.transaction.Transaction;
import org.poo.transaction.UpgradePlanTransaction;
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
    private final ExchangeRates exchangeRates;
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
    private static final int UPGRADE_LIMIT = 300;
    private static final int UPGRADE_COUNT = 5;

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
                            final String description,
                            final String commerciant,
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
     * Method to update the business account,
     * only if the user is associated with the business account.
     * It makes the necessary updates for the business account,
     * like checking the role of the user, adding the amount spent by the user,
     * adding details to the business report.
     *
     * @param account             the account
     * @param businessUser        the business user
     * @param amountToPay         the amount to pay
     * @param businessCommerciant the commerciant
     * @return true if the account was updated, false otherwise
     */
    public boolean updateBusinessAccount(final Account account,
                                         final User businessUser,
                                         final double amountToPay,
                                         final String businessCommerciant) {

        // cast the account to a business account
        BusinessAccount businessAccount = (BusinessAccount) account;

        // check if the user is the owner of the business account
        if (!businessAccount.getOwner().equals(businessUser) && amount != 0) {

            // get the commerciant report for the business account
            BusinessCommerciantReport businessCommerciantReport =
                    businessAccount.getBusinessCommerciantReport();

            // check if the user is a manager or an employee
            if (businessAccount.isManager(businessUser)) {
                // add the commerciant to the report
                CommerciantBusiness commerciantForReport =
                        businessCommerciantReport.getCommerciantBusiness(businessCommerciant);
                if (commerciantForReport == null) {
                    commerciantForReport = new CommerciantBusiness(businessCommerciant);
                    businessCommerciantReport.addCommerciantBusiness(commerciantForReport);
                }

                // get the commerciant business from the report
                CommerciantBusiness commerciantBusiness =
                        businessCommerciantReport.getCommerciantBusiness(businessCommerciant);

                // add the amount spent by the manager
                businessAccount.addManagerSpentAmount(businessUser, amount);
                businessAccount.setTotalSpent(businessAccount.getTotalSpent() + amount);

                // add the manager to the commerciant business
                commerciantBusiness.addManager(businessUser);
                commerciantBusiness.addAmountSpent(amount);

            } else if (businessAccount.isEmployee(businessUser)) {

                // check if the employee has the right to spend this amount
                if (amountToPay > businessAccount.getMaxSpendLimit()) {
                    return false;
                }

                // add the commerciant to the report
                CommerciantBusiness commerciantForReport =
                        businessCommerciantReport.getCommerciantBusiness(businessCommerciant);
                if (commerciantForReport == null) {
                    commerciantForReport = new CommerciantBusiness(businessCommerciant);
                    businessCommerciantReport.addCommerciantBusiness(commerciantForReport);
                }

                // get the commerciant business from the report
                CommerciantBusiness commerciantBusiness =
                        businessCommerciantReport.getCommerciantBusiness(businessCommerciant);
                businessAccount.addEmployeeSpentAmount(businessUser, amount);
                businessAccount.setTotalSpent(businessAccount.getTotalSpent() + amount);
                commerciantBusiness.addEmployee(businessUser);
                commerciantBusiness.addAmountSpent(amount);
            }
        }
        return true;
    }

    /**
     * Method to check if the commerciant will give cashback and if any discounts apply.
     * @param account the account
     * @param user the user
     * @param payAmount the amount
     * @param amountRon the amount in Ron
     * @param cardCurrency the card currency
     * @param existingCommerciant the existing commerciant
     */
    public void checkCashback(final Account account,
                              final User user,
                              final double payAmount,
                              final double amountRon,
                              final String cardCurrency,
                              final Commerciant existingCommerciant) {

        // check if there are any discounts for the commerciant to apply
        // Apply discounts based on commerciant type
        DiscountStrategy commerciantStrategy =
                DiscountStrategyFactory.getStrategy(existingCommerciant.getType());
        if (commerciantStrategy != null) {
            commerciantStrategy.applyDiscount(account, existingCommerciant, payAmount);
        }
        existingCommerciant.addAmountSpent(payAmount);

        // use the strategy pattern to apply the cashback
        // i saved the discounts in the account with this strategy
        CashbackManager cashbackManager = new CashbackManager();

        if (existingCommerciant.getCashbackStrategy().equals("spendingThreshold")) {
            account.addAmountSpentOnSTCommerciants(amountRon);
            cashbackManager.setStrategy(new SpendingThresholdCashback(user.getServicePlan()));
        } else if (existingCommerciant.getCashbackStrategy().equals("nrOfTransactions")) {
            cashbackManager.setStrategy(new NrOfTransactionsCashback());
        }

        cashbackManager.applyCashback(existingCommerciant,
                account, payAmount, cardCurrency, exchangeRates);

        // Apply the spending threshold discount
        DiscountStrategy spendingThresholdStrategy =
                DiscountStrategyFactory.getStrategy("SpendingThreshold");
        if (spendingThresholdStrategy != null) {
            spendingThresholdStrategy.applyDiscount(account, existingCommerciant, payAmount);
        }
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
            } else {
                // check if the user is the owner of the business account
                BusinessAccount businessAccount = (BusinessAccount) account;

                // check if the user is associated with the business account
                if (!businessAccount.isEmployee(businessUser)
                        && !businessAccount.isManager(businessUser)
                        && !businessAccount.getOwner().equals(businessUser)) {
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

        double amountToPay = user.addCommission(amount, exchangeRates, cardCurrency);
        // check if the account has enough funds
        if (account.getBalance() >= amountToPay) {
            if (account.getType().equals("business")) {
                // check if updating the business account was successful
                if (!updateBusinessAccount(account, businessUser,
                        amountToPay, commerciant)) {
                    return;
                }
            }
            // update the balance of the account
            account.setBalance(account.getBalance() - amountToPay);

            // create a transaction for the payment
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

            // check if the commerciant will give cashback and if any discounts apply
            checkCashback(account, user, amount, amountRon,
                    cardCurrency, existingCommerciant);

            // check if the user has to upgrade the service plan
            if (amountRon > UPGRADE_LIMIT && user.getServicePlan().equals("silver")) {
                user.incrementPaymentsOverThreeHundred();
                if (user.getPaymentsOverThreeHundred() == UPGRADE_COUNT) {
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
                ClassicAccount classicAccount = (ClassicAccount) account;
                ClassicReport report = classicAccount.getReport();
                PaymentsRecord paymentsRecord = classicAccount.getPaymentsRecord();
                report.addTransaction(transaction);
                paymentsRecord.addTransaction(transaction);
                Commerciant commerciantForSpendingReport = new Commerciant(commerciant,
                        amount, timestamp);
                classicAccount.addCommerciantForSpendingReport(commerciantForSpendingReport);
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
