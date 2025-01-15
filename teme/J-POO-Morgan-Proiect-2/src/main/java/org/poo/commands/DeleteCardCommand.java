package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.transaction.CardDestroyed;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import org.poo.card.Card;

/**
 * Command to delete a card from a user's account.
 */
public final class DeleteCardCommand implements Command {
    private final UserRegistry userRegistry;
    private final int timestamp;
    private final String email;
    private final String cardNumber;

    /**
     * Constructor for the DeleteCardCommand class.
     *
     * @param userRegistry the UserRegistry object
     * @param timestamp    the timestamp
     * @param email        the email of the user
     * @param cardNumber   the card number
     */
    public DeleteCardCommand(final UserRegistry userRegistry,
                             final int timestamp,
                             final String email,
                             final String cardNumber) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.email = email;
        this.cardNumber = cardNumber;
    }

    /**
     * Adds a transaction for the card destruction to the transactions list of the user
     * and report of the account.
     *
     * @param user    the user
     * @param account the account of the user
     */
    private void addTransaction(final User user, final Account account) {
        Transaction transaction = new CardDestroyed(timestamp,
                "The card has been destroyed", account.getIBAN(),
                cardNumber, user.getEmail());
        user.addTransaction(transaction);
        account.addTransaction(transaction);
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

        for (Account account : user.getAccounts()) {
            Card card = account.getCardByNumber(cardNumber);
            if (card != null && account.getBalance() == 0) {
                // Remove the card from the account
                account.getCards().remove(card);

                // Add the transaction to the transaction history and to the report
                addTransaction(user, account);
                return;
            }
        }
    }

}
