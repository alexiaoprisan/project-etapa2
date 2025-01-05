package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * SplitPaymentCustomTransaction class represents a custom split payment transaction.
 */
public final class SplitPaymentCustomTransactionError extends Transaction {

    private final List<Double> amountForUsers;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;
    private final String error;

    /**
     * Constructor for SplitPaymentCustomTransaction.
     *
     * @param timestamp        The timestamp of the transaction.
     * @param description      The description of the transaction.
     * @param amountForUsers   The amounts for each user involved in the transaction.
     * @param currency         The currency of the transaction.
     * @param involvedAccounts The accounts involved in the transaction.
     * @param splitPaymentType The type of split payment.
     */
    public SplitPaymentCustomTransactionError(final int timestamp,
                                         final String description,
                                         final List<Double> amountForUsers,
                                         final String currency,
                                         final List<String> involvedAccounts,
                                         final String splitPaymentType,
                                              final String error) {
        super(timestamp, description);
        this.amountForUsers = amountForUsers;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = splitPaymentType;
        this.error = error;
    }

    /**
     * Getter for amountForUsers.
     *
     * @return The amounts for each user involved in the transaction.
     */
    public List<Double> getAmountForUsers() {
        return amountForUsers;
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
     * Getter for splitPaymentType.
     *
     * @return The type of split payment.
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    /**
     * Getter for error.
     *
     * @return The error message.
     */
    public String getError() {
        return error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        ArrayNode amountsArray = node.putArray("amountForUsers");
        for (double amount : getAmountForUsers()) {
            amountsArray.add(amount);
        }
        node.put("currency", getCurrency());
        node.put("description", getDescription());
        node.put("error", getError());
        ArrayNode accountsArray = node.putArray("involvedAccounts");
        for (String account : getInvolvedAccounts()) {
            accountsArray.add(account);
        }
        node.put("splitPaymentType", getSplitPaymentType());
        node.put("timestamp", getTimestamp());
    }
}
