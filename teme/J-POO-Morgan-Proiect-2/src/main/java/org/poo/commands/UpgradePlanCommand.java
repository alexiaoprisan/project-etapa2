package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.Transaction;
import org.poo.transaction.UpgradePlanError;
import org.poo.user.User;
import org.poo.user.UserRegistry;

public class UpgradePlanCommand implements Command {
    private final UserRegistry userRegistry;
    private final String iban;
    private final String newServicePlan;
    private final int timestamp;
    private ArrayNode output;
    private ExchangeRates exchangeRates;

    public UpgradePlanCommand(final UserRegistry userRegistry,
                              final ArrayNode output,
                              final int timestamp,
                              final String iban,
                              final String newServicePlan,
                              final ExchangeRates exchangeRates) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.iban = iban;
        this.newServicePlan = newServicePlan;
        this.exchangeRates = exchangeRates;
    }


    @Override
    public void execute() {
        // get the user by iban
        User user = userRegistry.getUserByIBAN(iban);
        if (user == null) {
            ObjectNode error = output.addObject();
            error.put("command", "upgradePlan");
            ObjectNode outputNode = error.putObject("output");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            error.put("timestamp", timestamp);
            return;
        }

        Account account = user.getAccountByIBAN(iban);
        if (account == null) {
            Transaction transaction = new UpgradePlanError(timestamp, "Account not found");
            user.addTransaction(transaction);

            ObjectNode error = output.addObject();
            error.put("command", "upgradePlan");
            ObjectNode outputNode = error.putObject("output");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            error.put("timestamp", timestamp);
            return;
        }

        if (user.getServicePlan().equals("standard") && newServicePlan.equals("student")) {
            return;
        }

        if (user.getServicePlan().equals("student") && newServicePlan.equals("standard")) {
            return;
        }

        // check if the user has the new service plan, so we don't upgrade it again
        if (user.getServicePlan().equals(newServicePlan)) {
            Transaction transaction = new UpgradePlanError(timestamp, "The user already has the " + newServicePlan + " plan.");
            user.addTransaction(transaction);
            account.addTransaction(transaction);
            return;
        }

        if (newServicePlan.equals("silver") && user.getServicePlan().equals("gold")) {
            Transaction transaction = new UpgradePlanError(timestamp, "You cannot downgrade your plan.");
            user.addTransaction(transaction);
            return;
        }

        if ((newServicePlan.equals("student") || newServicePlan.equals("standard")) &&
                ((user.getServicePlan().equals("silver")) || user.getServicePlan().equals("gold"))) {
            Transaction transaction = new UpgradePlanError(timestamp, "You cannot upgrade your plan.");
            user.addTransaction(transaction);
            return;
        }

        double rate = exchangeRates.convertExchangeRate("RON", account.getCurrency());
        user.upgradeServicePlan(newServicePlan, rate, account, timestamp);


    }
}
