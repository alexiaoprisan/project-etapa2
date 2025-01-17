package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents an error that occurred while upgrading a plan.
 */
public final class UpgradePlanError extends Transaction {

    /**
     * Constructs a new UpgradePlanError.
     *
     * @param timestamp   the timestamp of the error
     * @param description the description of the error
     */
    public UpgradePlanError(final int timestamp, final String description) {
        super(timestamp, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
