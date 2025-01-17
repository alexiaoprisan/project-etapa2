package org.poo.card;

/**
 * This interface represents a card, which can be a regular card or a one-time pay card.
 */
public interface Card {

    /**
     * Getter for the card number.
     *
     * @return the card number.
     */
    String getCardNumber();

    /**
     * Getter for the card type.
     * The card type can be "regular" or "oneTimeCard".
     *
     * @return the card type.
     */
    String getType();

    /**
     * Getter for the card status.
     * The card status can be "active", "warning" or "frozen".
     *
     * @return the card status.
     */
    String getStatus();

    /**
     * Setter for the card status.
     *
     * @param status the new card status, can be changed after a transaction.
     */
    void setStatus(String status);

    /**
     * Setter for the card number.
     *
     * @param cardNumber the new card number.
     */
    void setCardNumber(String cardNumber);

    /**
     * Setter for the card type.
     *
     * @param type the new card type.
     */
    void setType(String type);

    /**
     * Getter for the card balance.
     * @param ownerEmail the email of the card owner.
     */
    void setOwnerEmail(String ownerEmail);

    /**
     * Getter for the card balance.
     * @return the card balance.
     */
    String getOwnerEmail();
}
