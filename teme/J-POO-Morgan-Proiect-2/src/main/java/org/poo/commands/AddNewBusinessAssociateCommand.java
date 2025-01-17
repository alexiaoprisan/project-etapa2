package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * Command to add a new business associate (manager or employee) to a business account.
 */
public final class AddNewBusinessAssociateCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final String email;
    private final String accountIban;
    private final String role;

    /**
     * Constructs an AddNewBusinessAssociateCommand.
     *
     * @param userRegistry the registry of users
     * @param output the output to write the result
     * @param timestamp the command timestamp
     * @param email the email of the user to be added
     * @param accountIban the IBAN of the business account
     * @param role the role of the user (manager or employee)
     */
    public AddNewBusinessAssociateCommand(
            final UserRegistry userRegistry,
            final ArrayNode output,
            final int timestamp,
            final String email,
            final String accountIban,
            final String role) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.accountIban = accountIban;
        this.role = role;
    }

    /**
     * Executes the command to add a business associate.
     */
    @Override
    public void execute() {
        // the user to be added as a manager or employee
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            return;
        }

        // get the account by IBAN
        Account account = userRegistry.getAccountByIBAN(accountIban);
        if (account == null) {
            return;
        }

        if (!"business".equals(account.getType())) {
            return;
        }

        BusinessAccount businessAccount = (BusinessAccount) account;

        if ("manager".equals(role)) {
            businessAccount.addManager(user);
        } else if ("employee".equals(role)) {
            businessAccount.addEmployee(user);
        }
    }
}
