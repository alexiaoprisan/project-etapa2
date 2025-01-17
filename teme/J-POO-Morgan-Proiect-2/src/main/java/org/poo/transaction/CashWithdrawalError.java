package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a cash withdrawal error transaction.
 */
public final class CashWithdrawalError extends Transaction {

    /**
     * Creates a new cash withdrawal error transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the error
     */
    public CashWithdrawalError(final int timestamp,
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
