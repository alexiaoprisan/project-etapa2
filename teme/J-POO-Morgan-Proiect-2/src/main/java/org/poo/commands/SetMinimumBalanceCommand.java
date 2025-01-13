package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * Command class for setting the minimum balance of an account.
 * The account needs to be a SavingsAccount.
 */
public final class SetMinimumBalanceCommand implements Command {
    private final UserRegistry userRegistry;
    private final int timestamp;
    private final String iban;
    private final double amount;

    /**
     * Constructor for the SetMinimumBalanceCommand class.
     *
     * @param userRegistry the UserRegistry object
     * @param timestamp    the timestamp
     * @param account      the IBAN of the account
     * @param amount       the minimum balance
     */
    public SetMinimumBalanceCommand(final UserRegistry userRegistry,
                                    final int timestamp,
                                    final String account,
                                    final double amount) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.iban = account;
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Account account = userRegistry.getAccountByIBAN(iban);
        if (account == null) {
            return;
        }

        User user = userRegistry.getUserByIBAN(iban);

        // Check if the account is a BusinessAccount, because only the owner can set the minimum balance
        if (account.getType().equals("business")) {
            BusinessAccount businessAccount = (BusinessAccount) account;
            User owner = businessAccount.getOwner();
            if (!owner.equals(user)) {
                return;
            }
        }

        // Set the minimum balance of the account
        account.setMinBalance(amount);

    }

}
