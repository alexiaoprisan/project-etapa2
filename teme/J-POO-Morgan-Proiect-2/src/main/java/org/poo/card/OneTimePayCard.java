package org.poo.card;

/**
 * This class represents a one-time pay card.
 * A one-time pay card can only be used once, then it is deleted
 * and a new card is created to replace it.
 */
public final class OneTimePayCard implements Card {
    private String cardNumber;
    private static String type = "oneTimePay";
    private String status;
    private String ownerEmail;

    private boolean used = false;

    /**
     * Constructor for a one-time pay card.
     * @param cardNumber The card number.
     */
    public OneTimePayCard(final String cardNumber, final String ownerEmail) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.ownerEmail = ownerEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(final String type) {
        this.type = type;
    }

    public void setOwnerEmail(final String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

}
