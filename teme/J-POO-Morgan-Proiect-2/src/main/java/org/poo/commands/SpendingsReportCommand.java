package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.ClassicAccount;
import org.poo.user.UserRegistry;
import org.poo.commerciants.Commerciant;
import org.poo.report.PaymentsRecord;
import org.poo.report.SpendingsReport;
import org.poo.user.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * Command class for generating a spendings report.
 * The spendings report is generated for a specific account between two timestamps.
 * It is available only for ClassicAccounts.
 */
public final class SpendingsReportCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String iban;

    /**
     * Constructor for the SpendingsReportCommand class.
     *
     * @param userRegistry   the UserRegistry object
     * @param output         the ArrayNode object
     * @param startTimestamp the start timestamp
     * @param endTimestamp   the end timestamp
     * @param account        the IBAN of the account
     * @param timestamp      the timestamp
     */
    public SpendingsReportCommand(final UserRegistry userRegistry,
                                  final ArrayNode output,
                                  final int startTimestamp,
                                  final int endTimestamp,
                                  final String account,
                                  final int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.iban = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Account account = userRegistry.getAccountByIBAN(iban);

        // if the account is not found, return an error message
        if (account == null) {
            ObjectNode node = output.addObject();
            node.put("command", "spendingsReport");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        // get the user associated with the account
        User user = userRegistry.getUserByIBAN(iban);

        // check if the account is a ClassicAccount, because the spendings report is
        // available only for ClassicAccounts
        if (account.getType().equals("classic")) {
            // convert the account to a ClassicAccount
            ClassicAccount classicAccount = (ClassicAccount) account;

            // get the payments record and the list of commerciants
            PaymentsRecord paymentsRecord = classicAccount.getPaymentsRecord();
            ArrayList<Commerciant> commerciantsList = classicAccount.getCommerciantList();

            // create a SpendingsReport object
            SpendingsReport spendingsReport = new SpendingsReport(paymentsRecord, commerciantsList);

            // generate the spendings report
            ObjectNode node =
                    spendingsReport.generateSpendingsReportBetweenTimestamps(startTimestamp,
                    endTimestamp, timestamp, account, commerciantsList);
            output.add(node);
        } else {
            // if the account is not a ClassicAccount, return an error message
            ObjectNode node = output.addObject();
            node.put("command", "spendingsReport");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("error", "This kind of report is not supported "
                    + "for a saving account");
            node.put("timestamp", timestamp);
        }

    }
}
