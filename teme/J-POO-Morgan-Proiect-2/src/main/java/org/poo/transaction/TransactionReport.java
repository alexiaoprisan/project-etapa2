package org.poo.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a report that contains a list of transactions for the user.
 * Will be used for command printTransactions.
 */
public final class TransactionReport {

    private final List<Transaction> transactions;

    /**
     * Constructs a TransactionReport with an empty list of transactions.
     */
    public TransactionReport() {
        transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction to the report.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Retrieves the list of transactions in the report.
     *
     * @return A list of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Generates a JSON report of all transactions.
     *
     * @param timestamp The timestamp to include in the report.
     * @return An ObjectNode representing the JSON report.
     */
    public ObjectNode generateReport(final int timestamp) {
        try {
            // Create the JSON report
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            output.put("command", "printTransactions");

            // go through each transaction and add it to the report
            ArrayNode transactionArray = output.putArray("output");
            for (Transaction transaction : transactions) {
                ObjectNode transactionNode = transactionArray.addObject();
                transaction.toJson(transactionNode);
            }

            output.put("timestamp", timestamp);
            return output;
        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
