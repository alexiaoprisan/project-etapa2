package org.poo.discounts;

import java.util.HashMap;
import java.util.Map;

/**
 * The DiscountStrategyFactory class provides a mechanism to retrieve the appropriate
 * DiscountStrategy based on the discount type.
 */
public final class DiscountStrategyFactory {

    private static final Map<String, DiscountStrategy> DISCOUNT_STRATEGIES = new HashMap<>();

    // Static block to initialize the map of strategies.
    static {
        DISCOUNT_STRATEGIES.put("Food", new FoodDiscountStrategy());
        DISCOUNT_STRATEGIES.put("Clothes", new ClothesDiscountStrategy());
        DISCOUNT_STRATEGIES.put("Tech", new TechDiscountStrategy());
        DISCOUNT_STRATEGIES.put("SpendingThreshold", new SpendingThresholdDiscountStrategy());
    }

    // Private constructor to prevent instantiation of the utility class.
    private DiscountStrategyFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Retrieves the DiscountStrategy based on the given discount type.
     *
     * @param discountType The type of discount (e.g., Food, Clothes, Tech, SpendingThreshold).
     * @return The DiscountStrategy corresponding to the discount type, or null if not found.
     */
    public static DiscountStrategy getStrategy(final String discountType) {
        return DISCOUNT_STRATEGIES.get(discountType);
    }
}
