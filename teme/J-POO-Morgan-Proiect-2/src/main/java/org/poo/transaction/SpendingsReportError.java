package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * SpendingsReportError class represents an error in the spendings report.
 */
public final class SpendingsReportError extends Transaction {

    /**
     * Constructor for SpendingsReportError.
     *
     * @param timestamp   The timestamp of the error.
     * @param description The description of the error.
     */
    public SpendingsReportError(final int timestamp,
                                final String description) {
        super(timestamp, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("error", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
