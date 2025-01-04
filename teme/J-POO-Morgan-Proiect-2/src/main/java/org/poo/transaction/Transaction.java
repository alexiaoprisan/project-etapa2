package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a general transaction.
 * This is an abstract base class designed to be extended by
 * specific transaction types.
 * The transactions will be included in reports for the account and user.
 */
public abstract class Transaction {

    private final int timestamp;
    private final String description;

    /**
     * Constructs a Transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description a description of the transaction
     */
    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Returns the timestamp of the transaction.
     *
     * @return the timestamp of the transaction
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the description of the transaction.
     *
     * @return the description of the transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converts the transaction details into JSON format.
     * Subclasses will provide the implementation for this method.
     *
     * @param node the ObjectNode to populate with transaction details
     */
    public abstract void toJson(ObjectNode node);
}
