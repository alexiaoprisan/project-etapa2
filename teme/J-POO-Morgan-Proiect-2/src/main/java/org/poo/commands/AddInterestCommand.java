package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.transaction.Transaction;
import org.poo.transaction.InterestRateIncome;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * Command to change the balance of an account by adding interest
 * to the balance of an account.
 * The account must be a savings account.
 */
public final class AddInterestCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private String accountIBAN;

    /**
     * Constructor for the AddInterestCommand.
     *
     * @param userRegistry The user registry.
     * @param output       The output array.
     * @param timestamp    The timestamp of the command.
     * @param accountIBAN  The IBAN of the account.
     */
    public AddInterestCommand(final UserRegistry userRegistry,
                              final ArrayNode output,
                              final int timestamp,
                              final String accountIBAN) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.accountIBAN = accountIBAN;
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

        User user = userRegistry.getUserByIBAN(accountIBAN);
        if (user == null) {
            return;
        }

        // check if the account is a savings account
        if (account.getType().equals("savings")) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            double interestRate = savingsAccount.getInterestRate();
            double sumAdded = savingsAccount.getBalance() * interestRate;
            double newSum = savingsAccount.getBalance() * interestRate
                    + savingsAccount.getBalance();
            savingsAccount.setBalance(newSum);

            Transaction transaction = new InterestRateIncome(timestamp,
                    "Interest rate income", sumAdded, savingsAccount.getCurrency());
            user.addTransaction(transaction);

        } else {
            // print an error message if the account is not a savings account
            ObjectNode node = output.addObject();
            node.put("command", "addInterest");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "This is not a savings account");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
        }
    }

}
