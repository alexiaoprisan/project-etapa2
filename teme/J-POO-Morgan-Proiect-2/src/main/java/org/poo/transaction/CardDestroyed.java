package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for a destroyed card.
 */
public final class CardDestroyed extends Transaction {

    private final String account;
    private final String card;
    private final String cardHolder;

    /**
     * Constructs a CardDestroyed transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the transaction
     * @param account     the account associated with the destroyed card
     * @param card        the destroyed card
     * @param cardHolder  the holder of the destroyed card
     */
    public CardDestroyed(final int timestamp, final String description, final String account,
                         final String card, final String cardHolder) {
        super(timestamp, description);
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("account", account);
        node.put("card", card);
        node.put("cardHolder", cardHolder);
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
