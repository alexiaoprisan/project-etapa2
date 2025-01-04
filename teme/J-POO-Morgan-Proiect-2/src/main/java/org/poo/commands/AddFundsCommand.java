package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.user.User;

/**
 * Command to add funds to an account.
 * It will search for the account with the given iban and add the amount to its balance.
 */
public final class AddFundsCommand implements Command {
    private final UserRegistry userRegistry;
    private final String iban;
    private final double amount;

    /**
     * Constructor for the AddFundsCommand.
     *
     * @param userRegistry the user registry
     * @param iban         the iban of the account
     * @param amount       the amount to add
     */
    public AddFundsCommand(final UserRegistry userRegistry,
                           final String iban,
                           final double amount) {
        this.userRegistry = userRegistry;
        this.iban = iban;
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        for (User user : userRegistry.getUsers()) {
            Account account = user.getAccountByIBAN(iban);
            if (account != null) {
                account.setBalance(account.getBalance() + amount);
                return;
            }
        }
    }

}
