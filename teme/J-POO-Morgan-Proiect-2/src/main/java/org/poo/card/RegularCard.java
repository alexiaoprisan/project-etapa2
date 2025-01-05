package org.poo.card;

/**
 * This class represents a regular card.
 * A regular card can be used multiple times, for transactions.
 */
public final class RegularCard implements Card {
    private String cardNumber;
    private static String type = "regular";
    private String status;
    private String ownerEmail;

    /**
     * Constructor for a regular card.
     *
     * @param cardNumber The card number, generated previously.
     */
    public RegularCard(final String cardNumber, final String ownerEmail) {
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
