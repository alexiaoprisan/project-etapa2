package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class InterestRateIncome extends Transaction {
    private double amount;
    private String currency;

    public InterestRateIncome(int timestamp, String description, double amount, String currency) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;

    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public void toJson(final ObjectNode node) {
        node.put("amount", getAmount());
        node.put("currency", getCurrency());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
