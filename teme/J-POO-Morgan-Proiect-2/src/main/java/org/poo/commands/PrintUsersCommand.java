package org.poo.commands;

import org.poo.account.Account;
import org.poo.user.UserRegistry;
import org.poo.user.User;
import org.poo.card.Card;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Command to print all users and their accounts and cards.
 */
public final class PrintUsersCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;

    /**
     * Constructor for the PrintUsersCommand.
     *
     * @param userRegistry The user registry.
     * @param output       The output.
     * @param timestamp    The timestamp.
     */
    public PrintUsersCommand(final UserRegistry userRegistry,
                             final ArrayNode output,
                             final int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        ObjectNode outputNode = output.addObject();
        outputNode.put("command", "printUsers");

        ArrayNode usersArray = outputNode.putArray("output");

        // Iterate over all users and print their information
        for (User user : userRegistry.getUsers()) {
            ObjectNode userNode = usersArray.addObject();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            if (user.userHasAccount()) { // Simplified boolean expression
                ArrayNode accountsArray = userNode.putArray("accounts");

                for (Account account : user.getAccounts()) {
                    ObjectNode accountNode = accountsArray.addObject();
                    accountNode.put("IBAN", account.getIBAN());
                    double balanceRounded = Math.round(account.getBalance() * 100.0) / 100.0;
                    accountNode.put("balance", balanceRounded);
                    accountNode.put("currency", account.getCurrency());
                    accountNode.put("type", account.getType());

                    if (account.hasCards()) { // Simplified boolean expression
                        ArrayNode cardsArray = accountNode.putArray("cards");

                        for (Card card : account.getCards()) {
                            ObjectNode cardNode = cardsArray.addObject();
                            cardNode.put("cardNumber", card.getCardNumber());
                            cardNode.put("status", card.getStatus());
                        }
                    } else {
                        accountNode.putArray("cards");
                    }
                }
            } else {
                userNode.putArray("accounts");
            }
        }
        outputNode.put("timestamp", timestamp);
    }
}
