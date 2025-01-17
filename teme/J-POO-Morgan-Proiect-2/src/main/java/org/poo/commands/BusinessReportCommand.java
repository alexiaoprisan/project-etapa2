package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.report.BusinessCommerciantReport;
import org.poo.report.BusinessTransactionReport;
import org.poo.user.UserRegistry;

/**
 * Command to generate a business report.
 */
public final class BusinessReportCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String accountIban;
    private final String type;
    private final int timestamp;

    /**
     * Constructor for the BusinessReportCommand.
     *
     * @param userRegistry   the user registry
     * @param output         the output
     * @param startTimestamp the start timestamp
     * @param endTimestamp   the end timestamp
     * @param accountIban    the account iban
     * @param type           the type (transaction or commerciant)
     * @param timestamp      the timestamp
     */
    public BusinessReportCommand(final UserRegistry userRegistry, final ArrayNode output,
                                 final int startTimestamp, final int endTimestamp,
                                 final String accountIban, final String type,
                                 final int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.accountIban = accountIban;
        this.type = type;
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
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
            ObjectNode report =
                    new BusinessTransactionReport().generateReportBetweenTimestamps(startTimestamp,
                            endTimestamp, timestamp, businessAccount);
            output.add(report);

        } else {
            // Generate a business spendings report
            BusinessCommerciantReport report = businessAccount.getBusinessCommerciantReport();
            ObjectNode reportNode = report.generateReportBetweenTimestamps(startTimestamp,
                    endTimestamp, timestamp, businessAccount);
            output.add(reportNode);
        }

    }
}
