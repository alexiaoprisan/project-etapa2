package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class represents a transaction involving interest rate income.
 * It extends the Transaction class and provides details about the amount and currency.
 */
public final class InterestRateIncome extends Transaction {
    private final double amount;
    private final String currency;

    /**
     * Constructs an InterestRateIncome transaction.
     *
     * @param timestamp the timestamp of the transaction.
     * @param description the description of the transaction.
     * @param amount the amount of interest income.
     * @param currency the currency of the interest income.
     */
    public InterestRateIncome(final int timestamp,
                              final String description,
                              final double amount,
                              final String currency) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Returns the amount of the interest rate income.
     *
     * @return the amount of interest rate income.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the currency of the interest rate income.
     *
     * @return the currency of interest rate income.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Converts the interest rate income transaction to a JSON format.
     *
     * @param node the ObjectNode to populate with transaction details.
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("amount", getAmount());
        node.put("currency", getCurrency());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
