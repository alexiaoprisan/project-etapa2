package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Transaction that represents a warning for a payment.
 */
public final class WarningForPay extends Transaction {

    /**
     * Constructor for the WarningForPay class.
     *
     * @param timestamp   The timestamp of the transaction.
     * @param description The description of the transaction.
     */
    public WarningForPay(final int timestamp,
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
