package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.BusinessAccount;
import org.poo.commerciants.Commerciant;
import org.poo.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessCommerciantReport {

    ArrayList<CommerciantBusiness> commerciantBusinesses = new ArrayList<>();

    public BusinessCommerciantReport() {
    }

    public CommerciantBusiness getCommerciantBusiness(String commerciant) {
        for (CommerciantBusiness commerciantBusiness : commerciantBusinesses) {
            if (commerciantBusiness.getCommerciant().equals(commerciant)) {
                return commerciantBusiness;
            }
        }
        return null;
    }

    public void addCommerciantBusiness(CommerciantBusiness commerciantBusiness) {
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


    public ArrayList<CommerciantBusiness> getCommerciantBusinesses() {
        return commerciantBusinesses;
    }



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

            //"commerciants": [
            //                {
            //                    "commerciant": "Amazon",
            //                    "employees": [
            //                        "Voinea Valentin"
            //                    ],
            //                    "managers": [
            //                    ],
            //                    "total received": 220.04999999999998
            //                },
            //                {

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
