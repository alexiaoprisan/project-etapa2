package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * Command to change the deposit limit of a business account.
 */
public final class ChangeDepositLimitCommand implements Command {

    private final UserRegistry userRegistry;
    private final int timestamp;
    private final String email;
    private final String accountIban;
    private final double amount;
    private final ArrayNode output;

    /**
     * Instantiates a new Change deposit limit command.
     *
     * @param userRegistry the user registry
     * @param output the output
     * @param timestamp the timestamp
     * @param accountIban the account IBAN
     * @param email the email
     * @param amount the amount
     */
    public ChangeDepositLimitCommand(final UserRegistry userRegistry, final ArrayNode output,
                                     final int timestamp, final String accountIban,
                                     final String email, final double amount) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.email = email;
        this.accountIban = accountIban;
        this.amount = amount;
        this.output = output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // get the user by email
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            return;
        }

        // get the account by IBAN
        Account account = userRegistry.getAccountByIBAN(accountIban);
        if (account == null) {
            return;
        }

        if (!account.getType().equals("business")) {
            return;
        }

        BusinessAccount businessAccount = (BusinessAccount) account;

        // check if the user is the owner of the account,
        // because only the owner can change the spending limit
        User owner = businessAccount.getOwner();
        if (!owner.equals(user)) {

            ObjectNode outputNode = output.addObject();
            outputNode.put("command", "changeDepositLimit");
            ObjectNode outputObject = outputNode.putObject("output");
            outputObject.put("description", "You must be owner in order to change deposit limit.");
            outputObject.put("timestamp", timestamp);
            outputNode.put("timestamp", timestamp);
            return;
        }

        businessAccount.setMaxDepositedLimit(amount);
    }
}
