package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.card.Card;
import org.poo.transaction.Transaction;
import org.poo.transaction.WarningForPay;
import org.poo.user.User;

/**
 * Command to check the status of a card.
 * The status of a card can be "active", "frozen" or "warning".
 */
public final class CheckCardStatusCommand implements Command {
    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final String cardNumber;
    private final int timestamp;

    private static final int MINIMUM_BALANCE_DIFFERENCE = 30;

    /**
     * Constructor for the CheckCardStatusCommand.
     *
     * @param userRegistry The user registry.
     * @param output       The output array.
     * @param cardNumber   The card number.
     * @param timestamp    The timestamp.
     */
    public CheckCardStatusCommand(final UserRegistry userRegistry,
                                  final ArrayNode output,
                                  final String cardNumber,
                                  final int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;

    }

    /**
     * Execute the command.
     */
    @Override
    public void execute() {
        Card card = userRegistry.getCardByNumber(cardNumber);

        if (card == null) {
            // Print an error message if the card is not found
            ObjectNode node = output.addObject();
            node.put("command", "checkCardStatus");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        User user = userRegistry.getUserByCardNumber(cardNumber);
        if (user == null) {
            return;
        }

        Account account = user.getAccountByCardNumber(cardNumber);
        if (account == null) {
            return;
        }

        // Check if the card needs to be frozen
        if (account.getBalance() - account.getMinBalance() < MINIMUM_BALANCE_DIFFERENCE) {
            //card.setStatus("frozen");

            // Create a new transaction and add it to the user and account
            Transaction transaction = new WarningForPay(timestamp,
                    "You have reached the minimum amount of funds, "
                            + "the card will be frozen");

            user.addTransaction(transaction);
            account.addTransaction(transaction);
        }


    }
}
