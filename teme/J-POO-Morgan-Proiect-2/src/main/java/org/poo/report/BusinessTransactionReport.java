package org.poo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessTransactionReport {

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
            accountNode.put("statistics type", "transaction");

            ArrayNode managersArray = accountNode.putArray("managers");

            List<User> managers = account.getManagers();
            Map<User, Double> managersSpent = account.getManagerSpentAmounts();
            Map<User, Double> managersDeposited = account.getManagerDepositedAmounts();

            for (User manager : managers) {
                ObjectNode managerNode = managersArray.addObject(); // Correctly adds an object to the array
                String username = manager.getLastName() + " " + manager.getFirstName();
                managerNode.put("username", username);
                managerNode.put("spent", managersSpent.getOrDefault(manager, 0.0)); // Use default if null
                managerNode.put("deposited", managersDeposited.getOrDefault(manager, 0.0)); // Use default if null
            }

            ArrayNode employeesArray = accountNode.putArray("employees");

            List<User> employees = account.getEmployees();
            Map<User, Double> employeesSpent = account.getEmployeeSpentAmount();
            Map<User, Double> employeesDeposited = account.getEmployeeDepositedAmounts();

            for (User employee : employees) {
                ObjectNode employeeNode = employeesArray.addObject();
                String username = employee.getLastName() + " " + employee.getFirstName();
                employeeNode.put("username", username);
                employeeNode.put("spent", employeesSpent.getOrDefault(employee, 0.0));
                employeeNode.put("deposited", employeesDeposited.getOrDefault(employee, 0.0));
            }

            accountNode.put("total spent", account.getTotalSpent());
            accountNode.put("total deposited", account.getTotalDeposited());

            output.put("timestamp", timestamp);
            return output;

        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }

    }


}
