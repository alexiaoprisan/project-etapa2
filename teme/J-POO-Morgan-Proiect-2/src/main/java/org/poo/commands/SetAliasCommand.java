package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.user.User;

/**
 * This class represents a command that sets an alias for an account,
 * The alias can be used when an account needs to send money to another account
 * and the user does not want to use the IBAN.
 */
public final class SetAliasCommand implements Command {
    private final UserRegistry userRegistry;
    private final String email;
    private final String iban;
    private final String alias;

    /**
     * Constructor for the SetAliasCommand class.
     *
     * @param userRegistry the UserRegistry object
     * @param timestamp    the timestamp
     * @param email        the email
     * @param iban         the IBAN
     * @param alias        the alias
     */
    public SetAliasCommand(final UserRegistry userRegistry,
                           final int timestamp,
                           final String email,
                           final String iban,
                           final String alias) {
        this.userRegistry = userRegistry;
        this.email = email;
        this.iban = iban;
        this.alias = alias;
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

        // Set the alias for the account
        account.setAlias(alias);

    }
}
