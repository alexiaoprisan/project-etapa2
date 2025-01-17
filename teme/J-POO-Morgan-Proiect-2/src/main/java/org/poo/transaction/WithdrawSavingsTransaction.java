package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a withdraw savings transaction.
 */
public final class WithdrawSavingsTransaction extends Transaction {

    /**
     * Constructor.
     * @param timestamp The timestamp of the transaction.
     * @param description The description of the transaction.
     */
    public WithdrawSavingsTransaction(final int timestamp, final String description) {
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
