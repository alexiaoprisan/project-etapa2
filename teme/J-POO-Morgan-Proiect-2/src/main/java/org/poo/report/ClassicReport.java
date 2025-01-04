package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Class which generates transaction reports for accounts.
 * The account has to be a classic account.
 */
public final class ClassicReport {

    // List of transactions
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
     * Retrieves the list of transactions.
     *
     * @return the list of transactions
     */
    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return a copy to ensure immutability
    }

    /**
     * Generates a report for transactions that occurred between
     * the specified timestamps.
     *
     * @param timestampStart the start timestamp
     * @param timestampEnd   the end timestamp
     * @param timestamp      the current timestamp of the report generation
     * @param account        the account for which the report is generated
     * @return the report as an ObjectNode
     */
    public ObjectNode generateReportBetweenTimestamps(
            final int timestampStart,
            final int timestampEnd,
            final int timestamp,
            final Account account) {
        try {
            // prepare the output JSON
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            output.put("command", "report");

            ObjectNode accountNode = output.putObject("output");
            accountNode.put("balance", account.getBalance());
            accountNode.put("currency", account.getCurrency());
            accountNode.put("IBAN", account.getIBAN());

            ArrayNode transactionArray = accountNode.putArray("transactions");

            // go through the transactions and add them to the report
            for (Transaction transaction : transactions) {
                // check if the transaction occurred between the specified timestamps
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
