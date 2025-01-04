package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a transaction for the creation of a new card.
 */
public final class NewCardCreatedTransaction extends Transaction {
    private final String iban;
    private final String cardNumber;
    private final String email;

    /**
     * Constructs a NewCardCreatedTransaction instance with the specified details.
     *
     * @param timestamp   The timestamp of the transaction.
     * @param description The description of the transaction.
     * @param iban        The IBAN associated with the card.
     * @param cardNumber  The number of the card.
     * @param email       The email of the cardholder.
     */
    public NewCardCreatedTransaction(final int timestamp,
                                     final String description,
                                     final String iban,
                                     final String cardNumber,
                                     final String email) {
        super(timestamp, description);
        this.iban = iban;
        this.cardNumber = cardNumber;
        this.email = email;
    }

    /**
     * Returns the IBAN associated with the card.
     *
     * @return The IBAN.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Returns the card number.
     *
     * @return The card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Returns the email of the cardholder.
     *
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("account", getIban());
        node.put("card", getCardNumber());
        node.put("cardHolder", getEmail());
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
