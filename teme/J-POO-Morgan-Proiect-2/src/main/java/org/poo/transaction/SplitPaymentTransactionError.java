package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Represents a transaction with an error in a split payment.
 */
public final class SplitPaymentTransactionError extends Transaction {

    private final double amount;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String error;
    private String splitPaymentType;

    /**
     * Creates an instance of ErrorSplitPaymentTransaction.
     *
     * @param timestamp        the timestamp of the transaction
     * @param description      the transaction description
     * @param amount           the transaction amount
     * @param currency         the transaction currency
     * @param involvedAccounts the accounts involved in the transaction
     * @param error            the error message
     */
    public SplitPaymentTransactionError(final int timestamp,
                                        final String description,
                                        final double amount,
                                        final String currency,
                                        final List<String> involvedAccounts,
                                        final String error,
                                        final String splitPaymentType) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
        this.splitPaymentType = splitPaymentType;
    }

    /**
     * Gets the error message of the transaction.
     *
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     * Gets the transaction amount.
     *
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the transaction currency.
     *
     * @return the transaction currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Gets the accounts involved in the transaction.
     *
     * @return an array of involved account IDs
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
        node.put("error", getError());
        node.put("currency", getCurrency());
        node.put("amount", getAmount());
        ArrayNode accountsArray = node.putArray("involvedAccounts");
        for (String account : getInvolvedAccounts()) {
            accountsArray.add(account);
        }
        node.put("splitPaymentType", splitPaymentType);

    }
}
