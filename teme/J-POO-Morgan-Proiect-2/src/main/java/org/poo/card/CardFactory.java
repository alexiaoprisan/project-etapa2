package org.poo.card;

/**
 * This class represents a card factory, which can create a card based on the card type.
 * The card type can be "regular" or "oneTimePay".
 * <p>
 * This class cannot be instantiated.
 */
public final class CardFactory {

    // Private constructor to prevent instantiation
    private CardFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Creates a card of the specified type.
     *
     * @param type       the type of card to create (regular or oneTimePay)
     * @param cardNumber the card number, which was generated previously
     * @return the created card
     */
    public static Card createCard(final String type, final String cardNumber, final String ownerEmail) {
        switch (type) {
            case "oneTimePay":
                return new OneTimePayCard(cardNumber, ownerEmail);
            case "regular":
                return new RegularCard(cardNumber, ownerEmail);
            default:
                throw new IllegalArgumentException("The card type " + type + " is not recognized.");
        }
    }
}
