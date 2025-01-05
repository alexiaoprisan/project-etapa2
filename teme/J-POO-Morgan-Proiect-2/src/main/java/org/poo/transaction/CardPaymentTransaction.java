package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a card payment transaction.
 * This class is designed to store details about a card payment.
 */
public final class CardPaymentTransaction extends Transaction {
    private final double amount;
    private final String commerciant;

    /**
     * Constructs a CardPaymentTransaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description a description of the transaction
     * @param amount      the amount of the payment
     * @param commerciant the name of the commerciant involved in the transaction
     */
    public CardPaymentTransaction(final int timestamp,
                                  final String description,
                                  final double amount,
                                  final String commerciant) {
        super(timestamp, description);
        this.amount = amount;
        this.commerciant = commerciant;
    }

    /**
     * Returns the amount of the payment.
     *
     * @return the amount of the payment
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the name of the commerciant.
     *
     * @return the name of the commerciant
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        double roundedAmount = Math.round(getAmount() * 100.0) / 100.0;
        node.put("amount", roundedAmount);
        node.put("commerciant", getCommerciant());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
