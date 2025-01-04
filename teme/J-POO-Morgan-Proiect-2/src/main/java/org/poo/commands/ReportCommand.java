package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.ClassicAccount;
import org.poo.account.SavingsAccount;
import org.poo.user.UserRegistry;
import org.poo.report.ClassicReport;
import org.poo.report.SavingsReport;
import org.poo.user.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Handles the report generation for accounts within a specific timestamp range.
 */
public final class ReportCommand implements Command {
    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String iban;
    private final int timestamp;

    /**
     * Constructs a new report command.
     *
     * @param userRegistry   the user registry
     * @param output         the output array node
     * @param startTimestamp the start timestamp
     * @param endTimestamp   the end timestamp
     * @param iban           the IBAN of the account
     * @param timestamp      the timestamp of the command
     */
    public ReportCommand(final UserRegistry userRegistry,
                         final ArrayNode output,
                         final int startTimestamp,
                         final int endTimestamp,
                         final String iban,
                         final int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.iban = iban;
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Account account = userRegistry.getAccountByIBAN(iban);
        if (account == null) {
            // print an error if the account was not found
            ObjectNode node = output.addObject();
            node.put("command", "report");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        User user = userRegistry.getUserByIBAN(iban);

        // generate the report based on the account type
        if (account.getType().equals("classic")) {
            // cast the account to a classic account
            ClassicAccount classicAccount = (ClassicAccount) account;
            ClassicReport classicReport = classicAccount.getReport();

            // print the report for the classic account
            ObjectNode node = classicReport.generateReportBetweenTimestamps(
                    startTimestamp, endTimestamp, timestamp, account);
            output.add(node);
        } else {
            // cast the account to a savings account
            SavingsAccount savingsAccount = (SavingsAccount) account;
            SavingsReport savingsReport = savingsAccount.getReport();

            // print the report for the savings account
            ObjectNode node = savingsReport.generateReportBetweenTimestamps(
                    startTimestamp, endTimestamp, timestamp, account);
            output.add(node);
        }
    }
}
