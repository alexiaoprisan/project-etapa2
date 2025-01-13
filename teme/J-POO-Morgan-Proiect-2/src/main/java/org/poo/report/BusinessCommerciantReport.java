package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.BusinessAccount;
import org.poo.commerciants.Commerciant;
import org.poo.user.User;

import java.util.List;
import java.util.Map;

public class BusinessCommerciantReport {

    public ObjectNode generateReportBetweenTimestamps(
            final int timestampStart,
            final int timestampEnd,
            final int timestamp,
            final BusinessAccount account) {

        // Create the JSON report

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            output.put("command", "businessReport");

            ObjectNode accountNode = output.putObject("output");

            accountNode.put("IBAN", account.getIBAN());
            accountNode.put("balance", account.getBalance());
            accountNode.put("currency", account.getCurrency());
            accountNode.put("spending limit", account.getMaxSpendLimit());
            accountNode.put("deposit limit", account.getMaxDepositedLimit());
            accountNode.put("statistics type", "commerciant");

            ArrayNode commerciantsArray = accountNode.putArray("commerciants");

            List<Commerciant> commerciants = account.getCommerciantList();

            for (Commerciant commerciant : commerciants) {

            }

            output.put("timestamp", timestamp);
            return output;

        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }

    }


}
