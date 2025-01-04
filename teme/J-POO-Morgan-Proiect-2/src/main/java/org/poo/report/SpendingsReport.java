package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Generates a spendings report based on a time range.
 * The account has to be a client account.
 * The report includes payments made by the account, when paying online,
 * including the commerciant and the amount spent.
 */
public final class SpendingsReport {

    // the payments record to generate the report from
    private final PaymentsRecord paymentsRecord;

    // the list of commerciants to include in the report
    private final ArrayList<Commerciant> commerciantsList;

    /**
     * Constructor for the SpendingsReport class.
     *
     * @param paymentsRecord   The payments record to generate the report from.
     * @param commerciantsList The list of commerciants to include in the report.
     */
    public SpendingsReport(final PaymentsRecord paymentsRecord,
                           final ArrayList<Commerciant> commerciantsList) {
        this.paymentsRecord = paymentsRecord;
        this.commerciantsList = commerciantsList;
    }

    /**
     * Getter for the payments record.
     *
     * @return The payments record.
     */
    public PaymentsRecord getPaymentsRecord() {
        return paymentsRecord;
    }

    /**
     * Getter for the list of commerciants.
     *
     * @return The list of commerciants.
     */
    public ArrayList<Commerciant> getCommerciantsList() {
        return commerciantsList;
    }

    /**
     * Generates a spendings report based on a time range.
     *
     * @param timestampStart Start of the time range.
     * @param timestampEnd   End of the time range.
     * @param timestamp      Current timestamp.
     * @param account        Account details.
     * @param commerciants   List of commerciants to include in the report.
     * @return JSON object containing the spendings report.
     */
    public ObjectNode generateSpendingsReportBetweenTimestamps(
            final int timestampStart,
            final int timestampEnd,
            final int timestamp,
            final Account account,
            final ArrayList<Commerciant> commerciants) {

        try {
            // create the JSON object
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            output.put("command", "spendingsReport");

            ObjectNode accountNode = output.putObject("output");
            accountNode.put("balance", account.getBalance());

            // add commerciants to the spendings report
            ArrayNode commerciantsArray = accountNode.putArray("commerciants");
            for (Commerciant commerciant : commerciants) {
                if (commerciant.getTimestamp() >= timestampStart
                        && commerciant.getTimestamp() <= timestampEnd) {
                    // check if the timestamp of the commerciant is within the time range
                    ObjectNode commerciantNode = commerciantsArray.addObject();
                    commerciantNode.put("commerciant", commerciant.getCommerciant());
                    commerciantNode.put("total", commerciant.getAmountSpent());
                }
            }

            accountNode.put("currency", account.getCurrency());
            accountNode.put("IBAN", account.getIBAN());

            ArrayNode transactionArray = accountNode.putArray("transactions");
            ArrayList<Transaction> record = paymentsRecord.getTransactions();

            // go through the transactions and add them to the report
            for (Transaction transaction : record) {
                // check if the transaction is within the time range
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
