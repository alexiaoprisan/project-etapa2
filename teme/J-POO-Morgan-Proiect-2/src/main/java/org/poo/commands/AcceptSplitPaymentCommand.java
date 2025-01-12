package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.splitPayment.SplitPayment;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.transaction.*;
import org.poo.user.User;
import org.poo.user.UserRegistry;

import java.util.List;

public class AcceptSplitPaymentCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private String email;
    private String splitPaymentType;
    private SplitPaymentsRegistry splitPaymentsRegistry;
    private ExchangeRates exchangeRates;

    public AcceptSplitPaymentCommand(final UserRegistry userRegistry,
                                     final ArrayNode output,
                                     final int timestamp,
                                     final String email,
                                     final String splitPaymentType,
                                     final SplitPaymentsRegistry splitPaymentsRegistry,
                                     final ExchangeRates exchangeRates) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.splitPaymentType = splitPaymentType;
        this.splitPaymentsRegistry = splitPaymentsRegistry;
        this.exchangeRates = exchangeRates;
    }


    @Override
    public void execute() {
        User userToAccept = userRegistry.getUserByEmail(email);
        if (userToAccept == null) {
            return;
        }

        userToAccept.acceptSplitPayment(splitPaymentType);

        SplitPayment splitPayment = splitPaymentsRegistry.getSplitPaymentByUserEmail(email);
        if (splitPayment == null) {
            return;
        }

        if (!splitPayment.checkIfAllUsersAcceptedPayment()) {
            // we cant do the split payment
            return;
        }

        List<User> usersInvolvedInSplitPayment = splitPayment.getUsers();
        List<Double> amountsForEachUser = splitPayment.getAmountForEachAccount();
        List<String> accountsIBAN = splitPayment.getAccountsIBAN();
        String currency = splitPayment.getCurrency();

        // auxiliar variable to check if the split payment is possible
        int canDoSplit = 1;

        // auxiliar variable to store the IBAN of the account with insufficient funds
        String poorAccount = "";

        for (int i = 0; i < usersInvolvedInSplitPayment.size(); i++) {

            Account account = userRegistry.getAccountByIBAN(accountsIBAN.get(i));
            if (account == null) {
                return;
            }

            double requiredAmount = amountsForEachUser.get(i);
            // check if the account has the same currency as the total amount
            if (!account.getCurrency().equals(currency)) {
                double exchangeRate = exchangeRates.convertExchangeRate(account.getCurrency(),
                        currency);
                if (exchangeRate == 0) {
                    return;
                }
                // calculate the amount to be deducted from the account
                requiredAmount = amountsForEachUser.get(i) / exchangeRate;
            }

            // check if the account has enough money to perform the split payment
            if (account.getBalance() < requiredAmount) {
                poorAccount = accountsIBAN.get(i);
                canDoSplit = 0;
                break;
            }
        }

        // if the split payment is not possible, log an error transaction for each account
        if (canDoSplit == 0) {

            for (int i = 0; i < usersInvolvedInSplitPayment.size(); i++) {

                Account account = userRegistry.getAccountByIBAN(accountsIBAN.get(i));

                User user = userRegistry.getUserByIBAN(accountsIBAN.get(i));

                double amount = amountsForEachUser.get(i);
                double totalAmount = splitPayment.getTotalAmount();

                if (splitPaymentType.equals("custom")) {
                    // create a transaction for each account
                    Transaction transaction = new SplitPaymentCustomTransactionError(
                            splitPayment.getTimestamp(),
                            String.format("Split payment of %.2f %s", totalAmount, currency), // Ensures two decimal places
                            amountsForEachUser,
                            currency,
                            accountsIBAN,
                            splitPaymentType,
                            String.format("Account %s has insufficient funds for a split payment.", poorAccount) // Proper formatting for the error message
                    );

                    // add the transaction to the user, for printTransaction
                    user.addTransaction(transaction);
                    // add the transaction to the account, for the report
                    account.addTransaction(transaction);

                } else {
                    Transaction transaction = new SplitPaymentTransactionError(
                            splitPayment.getTimestamp(),
                            String.format("Split payment of %.2f %s", totalAmount, currency), // Ensures two decimal places
                            amount,
                            currency,
                            accountsIBAN,
                            String.format("Account %s has insufficient funds for a split payment.", poorAccount), // Properly formatted error message
                            splitPaymentType
                    );
                    user.addTransaction(transaction);
                    // add the transaction to the account, for the report
                    account.addTransaction(transaction);
                }


            }
            splitPaymentsRegistry.removeSplitPayment(splitPayment);
            return; // Exit if any account does not have enough balance


        }

        // Deduct the amounts and log transactions
        for (int i = 0; i < usersInvolvedInSplitPayment.size(); i++) {
            Account account = userRegistry.getAccountByIBAN(accountsIBAN.get(i));

            double deductionAmount = amountsForEachUser.get(i);
            if (!account.getCurrency().equals(currency)) {
                double exchangeRate =
                        exchangeRates.convertExchangeRate(account.getCurrency(),
                                currency);
                deductionAmount = amountsForEachUser.get(i) / exchangeRate;
            }

            account.setBalance(account.getBalance() - deductionAmount);

            double amount = amountsForEachUser.get(i);
            double totalAmount = splitPayment.getTotalAmount();

            // Add transaction to the user for printTransaction
            User user = userRegistry.getUserByIBAN(account.getIBAN());
            String description = String.format("Split payment of %.2f %s",
                    totalAmount, currency);


            if (splitPaymentType.equals("custom")) {
                Transaction transaction = new SplitPaymentCustomTransaction(splitPayment.getTimestamp(), description, amountsForEachUser, currency, accountsIBAN, splitPaymentType);
                user.addTransaction(transaction);
            } else {
                Transaction transaction = new SplitPaymentTransaction(splitPayment.getTimestamp(), description, amount, currency, accountsIBAN);
                user.addTransaction(transaction);
            }
        }

        splitPaymentsRegistry.removeSplitPayment(splitPayment);

    }
}