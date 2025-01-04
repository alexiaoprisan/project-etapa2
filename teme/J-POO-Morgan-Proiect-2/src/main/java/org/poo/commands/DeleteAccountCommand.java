package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.transaction.ErrorDeleteAccount;
import org.poo.transaction.Transaction;
import org.poo.user.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Command to delete an account
 */
public final class DeleteAccountCommand implements Command {
    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final String email;
    private final String iban;

    /**
     * Constructor for the DeleteAccountCommand
     *
     * @param userRegistry the user registry
     * @param output       the output
     * @param timestamp    the timestamp
     * @param email        the email
     * @param iban         the iban
     */
    public DeleteAccountCommand(final UserRegistry userRegistry,
                                final ArrayNode output,
                                final int timestamp,
                                final String email,
                                final String iban) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.iban = iban;
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

        Account account = user.getAccountByIBAN(iban);
        if (account == null) {
            return;
        }

        // Check if the account has funds remaining
        if (account.getBalance() > 0) {
            // if the account has funds remaining, the account cannot be deleted
            // print an error message and return
            ObjectNode node = output.addObject();
            node.put("command", "deleteAccount");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("error", "Account couldn't be deleted "
                    + "- see org.poo.transactions for details");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);

            // add a transaction to the user for the error
            Transaction transaction = new ErrorDeleteAccount(timestamp,
                    "Account couldn't be deleted - there are funds remaining");
            user.addTransaction(transaction);
            return;
        } else {
            // if the account has no funds remaining, remove the account from the user
            user.getAccounts().remove(account);

            // print a success message
            ObjectNode node = output.addObject();
            node.put("command", "deleteAccount");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("success", "Account deleted");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }
    }
}
