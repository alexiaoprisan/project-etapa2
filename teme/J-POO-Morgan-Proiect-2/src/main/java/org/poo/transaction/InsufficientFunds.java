package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for insufficient funds.
 * Made to print an error when a transaction is attempted with insufficient funds.
 */
public final class InsufficientFunds extends Transaction {

    /**
     * Constructor for the InsufficientFunds class.
     *
     * @param timestamp   The timestamp of the transaction
     * @param description The description of the transaction
     */
    public InsufficientFunds(final int timestamp,
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
