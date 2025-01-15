package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

public class ChangeDepositLimitCommand implements Command {

    private UserRegistry userRegistry;
    private int timestamp;
    private String email;
    private String accountIban;
    private double amount;
    private ArrayNode output;

    public ChangeDepositLimitCommand(UserRegistry userRegistry, ArrayNode output, int timestamp,
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
            return;
        }

        BusinessAccount businessAccount = (BusinessAccount) account;

        // check if the user is the owner of the account, because only the owner can change the spending limit
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
