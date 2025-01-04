package org.poo.discounts;

import java.util.HashMap;
import java.util.Map;

public class DiscountStrategyFactory {
    private static final Map<String, DiscountStrategy> strategies = new HashMap<>();

    static {
        strategies.put("Food", new FoodDiscountStrategy());
        strategies.put("Clothes", new ClothesDiscountStrategy());
        strategies.put("Tech", new TechDiscountStrategy());
        strategies.put("SpendingThreshold", new SpendingThresholdDiscountStrategy());
    }

    public static DiscountStrategy getStrategy(String discountType) {
        return strategies.get(discountType);
    }
}
