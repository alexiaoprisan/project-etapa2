package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.WithdrawSavingsTransaction;
import org.poo.transaction.Transaction;
import org.poo.transaction.WithdrawSavingsTransactionSucces;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * WithdrawSavingsCommand class is responsible for executing
 * the command of withdrawing money from a savings account.
 */
public class WithdrawSavingsCommand implements Command {
    private final UserRegistry userRegistry;
    private final String iban;
    private final double amount;
    private final int timestamp;
    private final String currency;
    private final ArrayNode output;
    private final ExchangeRates exchangeRates;
    private static final int MINIMUM_AGE = 21;

    /**
     * Constructor of the WithdrawSavingsCommand class.
     *
     * @param userRegistry  the user registry
     * @param output        the output
     * @param timestamp     the timestamp
     * @param exchangeRates the exchange rates
     * @param iban          the iban
     * @param amount        the amount
     * @param currency      the currency
     */
    public WithdrawSavingsCommand(final UserRegistry userRegistry,
                                  final ArrayNode output,
                                  final int timestamp,
                                  final ExchangeRates exchangeRates,
                                  final String iban,
                                  final double amount,
                                  final String currency) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
        this.iban = iban;
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Executes the command of withdrawing money from a savings account.
     */
    @Override
    public void execute() {
        User user = userRegistry.getUserByIBAN(iban);
        if (user == null) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "Account not found.");
            user.addTransaction(transaction);
            return;
        }

        Account savingsAccount = user.getAccountByIBAN(iban);
        if (savingsAccount == null) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "Account not found.");
            user.addTransaction(transaction);
            return;
        }

        if (!savingsAccount.getType().equals("savings")) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "Account is not of type savings.");
            user.addTransaction(transaction);
            return;
        }

        // check if the user is over 21 years old
        double age = user.getAge();
        if (age < MINIMUM_AGE) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "You don't have the minimum age required.");
            user.addTransaction(transaction);
            return;
        }

        // search for the classic account to transfer the money
        int foundAccount = 0;
        Account classicAccount = null;
        for (Account acc : user.getAccounts()) {
            if (acc.getType().equals("classic") && acc.getCurrency().equals(currency)) {
                classicAccount = acc;
                foundAccount = 1;
                break;
            }
        }

        if (foundAccount == 0) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "You do not have a classic account.");
            user.addTransaction(transaction);
            savingsAccount.addTransaction(transaction);

            return;
        }

        double exchangeRate =
                exchangeRates.convertExchangeRate(savingsAccount.getCurrency(), currency);
        double amountToTransfer = amount * exchangeRate;

        // check if the savings account has enough money
        if (savingsAccount.getBalance() < amountToTransfer) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                    "Insufficient funds.");
            user.addTransaction(transaction);
            return;
        }

        savingsAccount.setBalance(savingsAccount.getBalance() - amountToTransfer);
        classicAccount.setBalance(classicAccount.getBalance() + amount);

        Transaction transaction = new WithdrawSavingsTransaction(timestamp,
                "Savings withdrawal.");

        Transaction transactionReport = new WithdrawSavingsTransactionSucces(timestamp,
                "Savings withdrawal", amount, classicAccount.getIBAN(),
                savingsAccount.getIBAN());
        savingsAccount.addTransaction(transactionReport);
        user.addTransaction(transactionReport);
        user.addTransaction(transactionReport);
    }
}
