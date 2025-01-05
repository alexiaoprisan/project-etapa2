package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * SplitPaymentCustomTransaction class represents a custom split payment transaction.
 */
public final class SplitPaymentCustomTransaction extends Transaction {

    private final List<Double> amountForUsers;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String splitPaymentType;

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
    public SplitPaymentCustomTransaction(final int timestamp,
                                         final String description,
                                         final List<Double> amountForUsers,
                                         final String currency,
                                         final List<String> involvedAccounts,
                                         final String splitPaymentType) {
        super(timestamp, description);
        this.amountForUsers = amountForUsers;
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = splitPaymentType;
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
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("timestamp", getTimestamp());
        node.put("description", getDescription());
        ArrayNode amountsArray = node.putArray("amountForUsers");
        for (double amount : getAmountForUsers()) {
            amountsArray.add(amount);
        }
        node.put("currency", getCurrency());
        ArrayNode accountsArray = node.putArray("involvedAccounts");
        for (String account : getInvolvedAccounts()) {
            accountsArray.add(account);
        }
        node.put("splitPaymentType", getSplitPaymentType());
    }
}
