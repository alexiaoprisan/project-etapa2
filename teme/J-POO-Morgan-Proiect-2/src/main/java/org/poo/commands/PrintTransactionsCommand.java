package org.poo.commands;

import org.poo.user.UserRegistry;
import org.poo.transaction.TransactionReport;
import org.poo.user.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Command to print the transactions of a user.
 * The transactions are stored in a transaction report and were added
 * when the user made a transaction or made operations with the card
 * and account.
 */
public final class PrintTransactionsCommand implements Command {
    private final UserRegistry userRegistry;
    private final String email;
    private final int timestamp;
    private final ArrayNode output;

    /**
     * Constructor for the PrintTransactionsCommand.
     *
     * @param userRegistry the user registry.
     * @param output       the output array node.
     * @param timestamp    the timestamp.
     * @param email        the email of the user.
     */
    public PrintTransactionsCommand(
            final UserRegistry userRegistry,
            final ArrayNode output,
            final int timestamp,
            final String email
    ) {
        this.userRegistry = userRegistry;
        this.email = email;
        this.timestamp = timestamp;
        this.output = output;
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

        // get the transaction report of the user and print it to the output
        TransactionReport transactionReport = user.getTransactionReport();
        ObjectNode node = transactionReport.generateReport(timestamp);
        output.add(node);
    }
}
