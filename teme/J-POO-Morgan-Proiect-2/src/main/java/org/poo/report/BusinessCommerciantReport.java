package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.BusinessAccount;
import org.poo.user.User;

import java.util.ArrayList;

/**
 * Represents a report of all commerciants in the business account.
 * It contains a list of CommerciantBusiness objects.
 * Each commerciant has listed its employees and managers who made transactions.
 */
public final class BusinessCommerciantReport {

    private ArrayList<CommerciantBusiness> commerciantBusinesses = new ArrayList<>();

    /**
     * Default constructor.
     */
    public BusinessCommerciantReport() {
    }

    /**
     * Constructor with commerciantBusinesses.
     *
     * @param commerciant the commerciant
     */
    public CommerciantBusiness getCommerciantBusiness(final String commerciant) {
        for (CommerciantBusiness commerciantBusiness : commerciantBusinesses) {
            if (commerciantBusiness.getCommerciant().equals(commerciant)) {
                return commerciantBusiness;
            }
        }
        return null;
    }

    /**
     * Add a commerciant to the report.
     *
     * @param commerciantBusiness the commerciant
     */
    public void addCommerciantBusiness(final CommerciantBusiness commerciantBusiness) {
        // Add the commerciant alphabetically by its name
        for (int i = 0; i < commerciantBusinesses.size(); i++) {
            if (commerciantBusinesses.get(i).getCommerciant()
                    .compareTo(commerciantBusiness.getCommerciant()) > 0) {
                commerciantBusinesses.add(i, commerciantBusiness);
                return;
            }
        }
        // If no earlier position was found, add it at the end
        commerciantBusinesses.add(commerciantBusiness);
    }


    /**
     * Generate a report of all commerciants in the business account.
     *
     * @param timestamp the timestamp of the report
     * @param account   the business account
     * @return the report as a JSON object
     */
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

            for (CommerciantBusiness commerciantBusiness : commerciantBusinesses) {
                ObjectNode commerciantNode = mapper.createObjectNode();
                commerciantNode.put("commerciant", commerciantBusiness.getCommerciant());
                commerciantNode.put("total received", commerciantBusiness.getTotalAmountSpent());

                ArrayNode employeesArray = commerciantNode.putArray("employees");
                for (User employee : commerciantBusiness.getEmployees()) {
                    String employeeName = employee.getLastName() + " " + employee.getFirstName();
                    employeesArray.add(employeeName);
                }

                ArrayNode managersArray = commerciantNode.putArray("managers");
                for (User manager : commerciantBusiness.getManagers()) {
                    String managerName = manager.getLastName() + " " + manager.getFirstName();
                    managersArray.add(managerName);
                }


                commerciantsArray.add(commerciantNode);
            }

            output.put("timestamp", timestamp);
            return output;

        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }

    }

}
