package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class UpgradePlanError extends Transaction {

    public UpgradePlanError(final int timestamp, final String description) {
        super(timestamp, description);
    }

    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
