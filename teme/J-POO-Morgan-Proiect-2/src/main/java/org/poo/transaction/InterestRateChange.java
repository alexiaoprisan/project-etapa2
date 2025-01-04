package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a change in the interest rate as a type of transaction.
 */
public final class InterestRateChange extends Transaction {

    /**
     *
     *
     * @param timestamp The timestamp of the transaction.
     * @param description The description of the transaction.
     */
    public InterestRateChange(final int timestamp, final String description) {
        super(timestamp, description);
    }

    /**
     * Converts the details of this InterestRateChange instance to JSON format.
     *
     * @param node The JSON node to which the details are added.
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
