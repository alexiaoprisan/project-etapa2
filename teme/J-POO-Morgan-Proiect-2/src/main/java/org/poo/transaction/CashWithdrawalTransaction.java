package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CashWithdrawalTransaction extends Transaction {
    private final double amount;

    public CashWithdrawalTransaction(int timestamp, String description, double amount) {
        super(timestamp, description);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void toJson(ObjectNode node) {
        node.put("amount", getAmount());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
