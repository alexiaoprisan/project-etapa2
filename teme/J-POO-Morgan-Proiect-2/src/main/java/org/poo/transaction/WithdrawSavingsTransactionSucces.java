package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for a successful withdrawal from a savings account.
 * The transaction includes the amount withdrawn, the associated classic account IBAN,
 * and the savings account IBAN.
 */
public final class WithdrawSavingsTransactionSucces extends Transaction {
    private final double amount;
    private final String classicAccountIban;
    private final String savingsAccountIban;

    /**
     * Constructs a WithdrawSavingsTransactionSucces object.
     *
     * @param timestamp the timestamp of the transaction.
     * @param description a description of the transaction.
     * @param amount the amount withdrawn.
     * @param classicAccountIban the IBAN of the classic account.
     * @param savingsAccountIban the IBAN of the savings account.
     */
    public WithdrawSavingsTransactionSucces(final int timestamp,
                                            final String description,
                                            final double amount,
                                            final String classicAccountIban,
                                            final String savingsAccountIban) {
        super(timestamp, description);
        this.amount = amount;
        this.classicAccountIban = classicAccountIban;
        this.savingsAccountIban = savingsAccountIban;
    }

    /**
     * Returns the amount withdrawn in the transaction.
     *
     * @return the withdrawn amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the IBAN of the classic account involved in the transaction.
     *
     * @return the classic account IBAN.
     */
    public String getClassicAccountIban() {
        return classicAccountIban;
    }

    /**
     * Returns the IBAN of the savings account involved in the transaction.
     *
     * @return the savings account IBAN.
     */
    public String getSavingsAccountIban() {
        return savingsAccountIban;
    }

    /**
     * Converts the WithdrawSavingsTransactionSucces object to JSON format.
     *
     * @param node the ObjectNode to populate with the transaction's details.
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
        node.put("amount", amount);
        node.put("classicAccountIBAN", classicAccountIban);
        node.put("savingsAccountIBAN", savingsAccountIban);
    }
}
