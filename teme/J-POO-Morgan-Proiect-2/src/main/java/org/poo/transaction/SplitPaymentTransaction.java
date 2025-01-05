package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * SplitPaymentTransaction class represents a split payment transaction.
 */
public final class SplitPaymentTransaction extends Transaction {

    private final double amount;
    private final String currency;
    private final List<String> involvedAccounts;

    /**
     * Constructor for SplitPaymentTransaction.
     *
     * @param timestamp        The timestamp of the transaction.
     * @param description      The description of the transaction.
     * @param amount           The amount of the transaction.
     * @param currency         The currency of the transaction.
     * @param involvedAccounts The accounts involved in the transaction.
     */
    public SplitPaymentTransaction(final int timestamp,
                                   final String description,
                                   final double amount,
                                   final String currency,
                                   final List<String> involvedAccounts) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
    }

    /**
     * Getter for amount.
     *
     * @return The amount of the transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for currency.
     *
     * @return The currency of the transaction.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Getter for involvedAccounts.
     *
     * @return The accounts involved in the transaction.
     */
    public List<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("timestamp", getTimestamp());
        node.put("description", getDescription());
        node.put("currency", getCurrency());
        node.put("amount", getAmount());
        ArrayNode accountsArray = node.putArray("involvedAccounts");
        for (String account : getInvolvedAccounts()) {
            accountsArray.add(account);
        }
    }

}
