package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

public class ChangeSpendingLimitCommand implements Command {

    private UserRegistry userRegistry;
    private int timestamp;
    private String email;
    private String accountIban;
    private double amount;
    private ArrayNode output;

    public ChangeSpendingLimitCommand(UserRegistry userRegistry, ArrayNode output, int timestamp,
                                      String accountIban, String email, double amount) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.email = email;
        this.accountIban = accountIban;
        this.amount = amount;
        this.output = output;
    }

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

            ObjectNode error = output.addObject();
            error.put("command", "changeSpendingLimit");
            ObjectNode outputNode = error.putObject("output");
            outputNode.put("description", "This is not a business account");
            outputNode.put("timestamp", timestamp);
            error.put("timestamp", timestamp);
            return;
        }

        BusinessAccount businessAccount = (BusinessAccount) account;

        // check if the user is the owner of the account, because only the owner can change the spending limit
        User owner = businessAccount.getOwner();
        if (!owner.equals(user)) {
            ObjectNode error = output.addObject();
            error.put("command", "changeSpendingLimit");
            ObjectNode outputNode = error.putObject("output");
            outputNode.put("description", "You must be owner in order to change spending limit.");
            outputNode.put("timestamp", timestamp);
            error.put("timestamp", timestamp);
            return;

        }

        businessAccount.setMaxSpendLimit(amount);
    }
}
