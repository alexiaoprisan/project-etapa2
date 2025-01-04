package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.user.UserRegistry;
import org.poo.transaction.InterestRateChange;
import org.poo.transaction.Transaction;
import org.poo.user.User;

/**
 * Command to change the interest rate of a savings account.
 */
public final class ChangeInterestRateCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private String accountIBAN;
    private double interestRate;

    public ChangeInterestRateCommand(final UserRegistry userRegistry,
                                     final ArrayNode output,
                                     final int timestamp,
                                     final String accountIBAN,
                                     final double interestRate) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.accountIBAN = accountIBAN;
        this.interestRate = interestRate;
    }

    /**
     * Create a new transaction for the interest rate change.
     * The transaction will be added to the user's transaction list and the account's report.
     *
     * @param user    the user that the account belongs to
     * @param account the account that the interest rate was changed
     */
    private void createTransactions(final User user, final Account account) {
        Transaction transaction = new InterestRateChange(timestamp,
                "Interest rate of the account changed to " + interestRate);
        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    /**
     * Execute the command.
     */
    @Override
    public void execute() {
        Account account = userRegistry.getAccountByIBAN(accountIBAN);
        if (account == null) {
            return;
        }

        // Check if the account is a savings account
        if (account.getType().equals("savings")) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.setInterestRate(interestRate);

            // Get the user by IBAN from the user registry
            User user = userRegistry.getUserByIBAN(accountIBAN);

            // Create the transaction for the interest rate change
            createTransactions(user, savingsAccount);
        } else {
            // Print an error message if the account is not a savings account
            ObjectNode node = output.addObject();
            node.put("command", "changeInterestRate");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "This is not a savings account");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
        }
    }
}
