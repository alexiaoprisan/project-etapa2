package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class WithdrawSavingsTransactionSucces extends Transaction {
    private final double amount;
    private final String classicAccountIban;
    private final String savingsAccountIban;

    public WithdrawSavingsTransactionSucces(final int timestamp, final String description, final double amount, final String classicAccountIban, final String savingsAccountIban) {
        super(timestamp, description);
        this.amount = amount;
        this.classicAccountIban = classicAccountIban;
        this.savingsAccountIban = savingsAccountIban;
    }

    public double getAmount() {
        return amount;
    }

    public String getClassicAccountIban() {
        return classicAccountIban;
    }

    public String getSavingsAccountIban() {
        return savingsAccountIban;
    }

    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
        node.put("amount", amount);
        node.put("classicAccountIBAN", classicAccountIban);
        node.put("savingsAccountIBAN", savingsAccountIban);
    }
}
