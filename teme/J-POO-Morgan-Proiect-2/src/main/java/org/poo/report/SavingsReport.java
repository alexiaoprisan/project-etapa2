package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Represents a report generator for savings accounts.
 * The transactions come from changing of interest rates and adding
 * the interest to the account.
 */
public final class SavingsReport {

    private final ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Adds a transaction to the report.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Returns the list of transactions.
     *
     * @return the list of transactions
     */
    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return a copy for immutability
    }

    /**
     * Generates a savings report for transactions between two timestamps.
     *
     * @param timestampStart the start of the time range
     * @param timestampEnd   the end of the time range
     * @param timestamp      the current timestamp
     * @param account        the account for which the report is generated
     * @return the JSON report as an ObjectNode
     */
    public ObjectNode generateReportBetweenTimestamps(final int timestampStart,
                                                      final int timestampEnd,
                                                      final int timestamp,
                                                      final Account account) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            output.put("command", "report");

            ObjectNode accountNode = output.putObject("output");
            accountNode.put("balance", account.getBalance());
            accountNode.put("currency", account.getCurrency());
            accountNode.put("IBAN", account.getIBAN());

            ArrayNode transactionArray = accountNode.putArray("transactions");

            for (Transaction transaction : transactions) {
                if (transaction.getTimestamp() >= timestampStart
                        && transaction.getTimestamp() <= timestampEnd) {
                    ObjectNode transactionNode = transactionArray.addObject();
                    transaction.toJson(transactionNode);
                }
            }

            output.put("timestamp", timestamp);
            return output;
        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
