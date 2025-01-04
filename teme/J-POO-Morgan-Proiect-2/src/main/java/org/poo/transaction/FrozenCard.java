package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction where a card is frozen.
 */
public final class FrozenCard extends Transaction {

    /**
     * Creates a new FrozenCard transaction.
     * @param timestamp The timestamp of the transaction.
     * @param description The description of the transaction.
     */
    public FrozenCard(final int timestamp,
                      final String description) {
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
