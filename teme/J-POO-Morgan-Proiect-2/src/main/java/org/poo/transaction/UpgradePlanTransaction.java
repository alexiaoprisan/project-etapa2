package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class UpgradePlanTransaction extends Transaction {
    private String newServicePlan;
    private String account;

    public UpgradePlanTransaction(final int timestamp, final String description, final String newServicePlan, final String account) {
        super(timestamp, description);
        this.newServicePlan = newServicePlan;
        this.account = account;
    }

    public String getNewServicePlan() {
        return newServicePlan;
    }

    public String getAccount() {
        return account;
    }

    @Override
    public void toJson(final ObjectNode node) {
        node.put("accountIBAN", getAccount());
        node.put("description", getDescription());
        node.put("newPlanType", getNewServicePlan());
        node.put("timestamp", getTimestamp());
    }
}
