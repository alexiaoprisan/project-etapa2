package org.poo.report;

import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Handles recording and reporting of payment transactions for an account.
 * The account has to be a classic one, because the spending report is not
 * available for the savings account.
 * The transactions were made in payOnline commands.
 */
public final class PaymentsRecord {

    // The list of transactions in the record
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Adds a transaction to the record.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Retrieves all transactions in the record.
     *
     * @return a list of transactions
     */
    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return a defensive copy
    }
}
