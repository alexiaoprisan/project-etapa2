package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.transaction.NewCardCreatedTransaction;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.utils.Utils;

/**
 * Command for creating a new card.
 */
public final class CreateCardCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final String email;
    private final String iban;
    private final String command;

    public CreateCardCommand(final UserRegistry userRegistry,
                             final ArrayNode output,
                             final int timestamp,
                             final String email,
                             final String iban,
                             final String command) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.iban = iban;
        this.command = command;
    }

    /**
     * Adds a transaction to the user transactions list and the account report.
     *
     * @param user       the user
     * @param account    the account where the card was created
     * @param cardNumber the card number
     */
    private void addTransaction(final User user, final Account account,
                                final String cardNumber) {
        Transaction transaction = new NewCardCreatedTransaction(timestamp,
                "New card created", iban, cardNumber, email);
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

        // get the type of the card from the command name
        String type;
        if (command.equals("createCard")) {
            type = "regular";
        } else {
            type = "oneTimePay";
        }

        // generate a card number
        String cardNumber = Utils.generateCardNumber();

        // create the card
        for (Account acc : user.getAccounts()) {
            if (acc.getIBAN().equals(iban)) {
                acc.createCard(type, cardNumber);
            }
        }

        Account account = user.getAccountByIBAN(iban);

        // add the transactions to the user transactions list and the account report
        addTransaction(user, account, cardNumber);

    }
}
