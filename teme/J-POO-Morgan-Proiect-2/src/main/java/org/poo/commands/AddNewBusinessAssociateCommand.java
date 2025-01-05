package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

public class AddNewBusinessAssociateCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final String email;
    private final String accountIban;
    private final String role;

    public AddNewBusinessAssociateCommand(UserRegistry userRegistry, ArrayNode output, int timestamp, String email, String accountIban, String role) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.accountIban = accountIban;
        this.role = role;
    }

    @Override
    public void execute() {
        // the user which will be added as a manager or employee
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

        if (role.equals("manager")) {
            businessAccount.addManager(user);
        } else if (role.equals("employee")) {
            businessAccount.addEmployee(user);
        }
    }
}
