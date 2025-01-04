package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.user.UserRegistry;
import org.poo.transaction.ErrorSplitPaymentTransaction;
import org.poo.transaction.SplitPaymentTransaction;
import org.poo.transaction.Transaction;
import org.poo.user.User;

import java.util.List;

/**
 * Command to split a payment between multiple accounts.
 * Checks if the accounts exist and have enough balance to perform the split payment.
 */
public final class SplitPaymentCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private double totalAmount;
    private String currency;
    private List<String> accountsIBAN;
    private ExchangeRates exchangeRates;


    /**
     * Constructor for the SplitPaymentCommand class.
     *
     * @param userRegistry  the UserRegistry
     * @param output        the output ArrayNode
     * @param timestamp     the timestamp of the command
     * @param totalAmount   the total amount to be split
     * @param currency      the currency of the total amount
     * @param accounts      the list of IBANs of the accounts
     * @param exchangeRates the ExchangeRates object
     */
    public SplitPaymentCommand(final UserRegistry userRegistry,
                               final ArrayNode output,
                               final int timestamp,
                               final double totalAmount,
                               final String currency,
                               final List<String> accounts,
                               final ExchangeRates exchangeRates) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.accountsIBAN = accounts;
        this.exchangeRates = exchangeRates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // Calculate the amount to be deducted from each account
        double amount = totalAmount / accountsIBAN.size();

        // auxiliar variable to check if the split payment is possible
        int canDoSplit = 1;

        // auxiliar variable to store the IBAN of the account with insufficient funds
        String poorAccount = "";

        // Validate all accounts exist and check if the split payment is possible
        for (String accountIBAN : accountsIBAN) {

            // get the account
            Account account = userRegistry.getAccountByIBAN(accountIBAN);
            if (account == null) {
                return;
            }

            double requiredAmount = amount;
            // check if the account has the same currency as the total amount
            if (!account.getCurrency().equals(currency)) {
                double exchangeRate = exchangeRates.convertExchangeRate(account.getCurrency(),
                        currency);
                if (exchangeRate == 0) {
                    return;
                }
                // calculate the amount to be deducted from the account
                requiredAmount = amount / exchangeRate;
            }

            // check if the account has enough money to perform the split payment
            if (account.getBalance() < requiredAmount) {
                poorAccount = accountIBAN;
                canDoSplit = 0;
            }
        }

        // if the split payment is not possible, log an error transaction for each account
        if (canDoSplit == 0) {

            for (String accountIBAN : accountsIBAN) {

                User user = userRegistry.getUserByIBAN(accountIBAN);

                // create a transaction for each account
                Transaction transaction = new ErrorSplitPaymentTransaction(
                        timestamp, String.format("Split payment of %.2f %s", totalAmount, currency),
                        amount, currency, accountsIBAN.toArray(new String[0]),
                        "Account " + poorAccount + " has insufficient funds "
                                + "for a split payment."
                );
                // add the transaction to the user, for printTransaction
                user.addTransaction(transaction);
                // add the transaction to the account, for the report
                Account account = userRegistry.getAccountByIBAN(accountIBAN);
                account.addTransaction(transaction);
            }

            return; // Exit if any account does not have enough balance
        }

        // Deduct the amounts and log transactions
        for (String accountIBAN : accountsIBAN) {
            Account account = userRegistry.getAccountByIBAN(accountIBAN);

            double deductionAmount = amount;
            if (!account.getCurrency().equals(currency)) {
                double exchangeRate =
                        exchangeRates.convertExchangeRate(account.getCurrency(),
                                currency);
                deductionAmount = amount / exchangeRate;
            }

            account.setBalance(account.getBalance() - deductionAmount);

            // Add transaction to the user for printTransaction
            User user = userRegistry.getUserByIBAN(account.getIBAN());
            String description = String.format("Split payment of %.2f %s",
                    totalAmount, currency);
            Transaction transaction = new SplitPaymentTransaction(
                    timestamp, description, amount, currency, accountsIBAN.toArray(new String[0])
            );
            user.addTransaction(transaction);
        }
    }


}
