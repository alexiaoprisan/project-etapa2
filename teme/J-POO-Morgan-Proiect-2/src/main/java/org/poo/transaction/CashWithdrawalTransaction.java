package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a cash withdrawal transaction.
 */
public final class CashWithdrawalTransaction extends Transaction {
    private final double amount;

    /**
     * Creates a new cash withdrawal transaction.
     *
     * @param timestamp the timestamp of the transaction
     * @param description the description of the transaction
     * @param amount the amount of the cash withdrawal
     */
    public CashWithdrawalTransaction(final int timestamp,
                                     final String description,
                                     final double amount) {
        super(timestamp, description);
        this.amount = amount;
    }

    /**
     * Returns the amount of the cash withdrawal.
     *
     * @return the amount of the cash withdrawal
     */
    public double getAmount() {
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("amount", getAmount());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
