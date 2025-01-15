package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.report.BusinessCommerciantReport;
import org.poo.report.BusinessTransactionReport;
import org.poo.user.UserRegistry;

public class BusinessReportCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String accountIban;
    private final String type;
    private final int timestamp;

    public BusinessReportCommand(UserRegistry userRegistry, ArrayNode output, int startTimestamp, int endTimestamp, String accountIban, String type, int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.accountIban = accountIban;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {

        Account account = userRegistry.getAccountByIBAN(accountIban);
        if (account == null) {
            return;
        }

        if (!account.getType().equals("business")) {
            return;
        }

        BusinessAccount businessAccount = (BusinessAccount) account;

        if (type.equals("transaction")) {
            // Generate a business transaction report
            ObjectNode report = new BusinessTransactionReport().generateReportBetweenTimestamps(startTimestamp, endTimestamp, timestamp, businessAccount);
            output.add(report);

            // output.add(report.generateReportBetweenTimestamps(startTimestamp, endTimestamp, timestamp, account));
        } else {
            // Generate a business spendings report
            BusinessCommerciantReport report = businessAccount.getBusinessCommerciantReport();
            ObjectNode reportNode = report.generateReportBetweenTimestamps(startTimestamp, endTimestamp, timestamp, businessAccount);
            output.add(reportNode);
        }

    }
}
