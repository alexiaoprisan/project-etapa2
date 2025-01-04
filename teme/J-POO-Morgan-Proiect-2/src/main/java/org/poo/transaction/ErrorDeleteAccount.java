package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Transaction that represents an error when trying to delete an account.
 */
public final class ErrorDeleteAccount extends Transaction {

    /**
     * Constructor for the ErrorDeleteAccount class.
     *
     * @param timestamp   The timestamp of the transaction.
     * @param description The description of the error.
     */
    public ErrorDeleteAccount(final int timestamp, final String description) {
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
