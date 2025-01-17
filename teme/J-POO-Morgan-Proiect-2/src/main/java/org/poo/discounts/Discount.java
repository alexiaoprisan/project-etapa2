package org.poo.discounts;

/**
 * The Discount class represents a discount with a value, type, and usage status.
 * It provides methods to get and set the discount value, type, and usage state.
 */
public final class Discount {
    private double value;
    private String type; // food, clothes, or tech
    private boolean isUsed = false;

    /**
     * Constructs a Discount with a specified value and type.
     *
     * @param value The value of the discount.
     * @param type  The type of discount (e.g., food, clothes, tech).
     */
    public Discount(final double value, final String type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Gets the value of the discount.
     *
     * @return The value of the discount.
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the discount.
     *
     * @param value The new value of the discount.
     */
    public void setValue(final double value) {
        this.value = value;
    }

    /**
     * Gets the type of discount.
     *
     * @return The type of discount (e.g., food, clothes, tech).
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of discount.
     *
     * @param type The new type of discount (e.g., food, clothes, tech).
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Checks if the discount has been used.
     *
     * @return true if the discount is used, false otherwise.
     */
    public boolean checkIUsed() {
        return isUsed;
    }

    /**
     * Sets the usage state of the discount.
     *
     * @param wasUsed The new usage state (true if used, false if not).
     */
    public void setUsed(final boolean wasUsed) {
        this.isUsed = wasUsed;
    }

    /**
     * Marks the discount as used.
     */
    public void use() {
        this.isUsed = true;
    }
}
