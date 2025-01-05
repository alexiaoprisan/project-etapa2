package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.transaction.NewAccountCreated;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import org.poo.utils.Utils;

/**
 * Command to create a new account for a user.
 * A user holds multiple accounts.
 */
public final class AddAccountCommand implements Command {
    private final UserRegistry userRegistry;
    private final int timestamp;
    private final String email;
    private final String accountType;
    private final String currency;
    final double interestRate;

    /**
     * Constructor for the AddAccountCommand.
     *
     * @param userRegistry the user registry
     * @param timestamp    the timestamp of the command
     * @param email        the email of the user
     * @param accountType  the type of the account (classic or savings)
     * @param currency     the currency of the account
     */
    public AddAccountCommand(final UserRegistry userRegistry,
                             final int timestamp,
                             final String email,
                             final String accountType,
                             final String currency,
                             final double interestRate) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.email = email;
        this.accountType = accountType;
        this.currency = currency;
        this.interestRate = interestRate;
    }

    /**
     * Create a new transaction for the new account.
     * This transaction will be added to the user's transaction list and the account's report.
     *
     * @param user    the user that the account belongs to
     * @param account the account that was created
     */
    private void createTransactions(final User user, final Account account) {
        Transaction transaction = new NewAccountCreated(timestamp, "New account created");
        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        // Get the user from the user registry by email
        User user = userRegistry.getUserByEmail(email);

        String iban = Utils.generateIBAN();
        user.addAccount(accountType, currency, iban, interestRate, user);

        Account account = user.getAccountByIBAN(iban);

        // Create a new transaction for the new account
        createTransactions(user, account);

    }
}
