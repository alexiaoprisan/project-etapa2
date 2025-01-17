package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class represents a transaction where the user upgrades their service plan.
 * It contains the details of the new service plan and the associated account.
 */
public final class UpgradePlanTransaction extends Transaction {
    private final String newServicePlan;
    private final String account;

    /**
     * Constructs an UpgradePlanTransaction.
     *
     * @param timestamp the timestamp of the transaction.
     * @param description a description of the transaction.
     * @param newServicePlan the new service plan being upgraded to.
     * @param account the account involved in the upgrade.
     */
    public UpgradePlanTransaction(final int timestamp,
                                  final String description,
                                  final String newServicePlan,
                                  final String account) {
        super(timestamp, description);
        this.newServicePlan = newServicePlan;
        this.account = account;
    }

    /**
     * Returns the new service plan associated with this transaction.
     *
     * @return the new service plan.
     */
    public String getNewServicePlan() {
        return newServicePlan;
    }

    /**
     * Returns the account associated with the transaction.
     *
     * @return the account number.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Converts the UpgradePlanTransaction to a JSON format.
     *
     * @param node the ObjectNode to populate with transaction details.
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("accountIBAN", getAccount());
        node.put("description", getDescription());
        node.put("newPlanType", getNewServicePlan());
        node.put("timestamp", getTimestamp());
    }
}
