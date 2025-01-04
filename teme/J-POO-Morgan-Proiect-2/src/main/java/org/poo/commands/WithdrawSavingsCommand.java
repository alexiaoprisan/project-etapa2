package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.WithdrawSavingsTransaction;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import org.poo.user.UserRegistry;

public class WithdrawSavingsCommand implements Command {
    private final UserRegistry userRegistry;
    private final String iban;
    private final double amount;
    private final int timestamp;
    private String currency;
    private ArrayNode output;
    private ExchangeRates exchangeRates;

    public WithdrawSavingsCommand(final UserRegistry userRegistry,
                                  ArrayNode output,
                                  int timestamp,
                                  ExchangeRates exchangeRates,
                                  String iban,
                                  double amount,
                                  String currency) {
        this.userRegistry = userRegistry;
        this.iban = iban;
        this.amount = amount;
        this.timestamp = timestamp;
        this.currency = currency;
        this.output = output;
        this.exchangeRates = exchangeRates;

    }

    @Override
    public void execute() {
        User user = userRegistry.getUserByIBAN(iban);
        if (user == null) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "Account not found.");
            user.addTransaction(transaction);
            return;
        }

        Account savingsAccount = user.getAccountByIBAN(iban);
        if (savingsAccount == null) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "Account not found.");
            user.addTransaction(transaction);
            return;
        }

        if (!savingsAccount.getType().equals("savings")) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "Account is not of type savings.");
            user.addTransaction(transaction);
            return;
        }

        // check if the user is over 21 years old
        double age = user.getAge();
        if (age < 21) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "You don't have the minimum age required.");
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
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "You do not have a classic savingsAccount.");
            user.addTransaction(transaction);
            return;
        }

        double exchangeRate = exchangeRates.convertExchangeRate(savingsAccount.getCurrency(), currency);
        double amountToTransfer = amount * exchangeRate;

        // check if the savings account has enough money
        if (savingsAccount.getBalance() < amountToTransfer) {
            Transaction transaction = new WithdrawSavingsTransaction(timestamp, "Insufficient funds.");
            user.addTransaction(transaction);
            return;
        }

        savingsAccount.setBalance(savingsAccount.getBalance() - amountToTransfer);
        classicAccount.setBalance(classicAccount.getBalance() + amount);

        Transaction transaction = new WithdrawSavingsTransaction(timestamp, "Savings withdrawal.");
        user.addTransaction(transaction);

    }
}
