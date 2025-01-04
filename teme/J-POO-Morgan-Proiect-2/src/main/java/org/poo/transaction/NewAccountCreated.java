package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a new account creation as a type of transaction.
 */
public final class NewAccountCreated extends Transaction {

    /**
     * Creates a new account creation transaction.
     *
     * @param timestamp   The timestamp of the transaction.
     * @param description The description of the transaction.
     */
    public NewAccountCreated(final int timestamp,
                             final String description) {
        super(timestamp, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
