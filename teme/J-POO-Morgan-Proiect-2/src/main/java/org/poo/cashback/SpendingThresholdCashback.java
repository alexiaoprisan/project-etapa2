package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.exchangeRates.ExchangeRates;

public final class SpendingThresholdCashback implements CashbackStrategy {

    // Constants for thresholds
    private static final double GOLD_THRESHOLD_500 = 0.007;
    private static final double SILVER_THRESHOLD_500 = 0.005;
    private static final double DEFAULT_THRESHOLD_500 = 0.0025;

    private static final double GOLD_THRESHOLD_300 = 0.0055;
    private static final double SILVER_THRESHOLD_300 = 0.004;
    private static final double DEFAULT_THRESHOLD_300 = 0.002;

    private static final double GOLD_THRESHOLD_100 = 0.005;
    private static final double SILVER_THRESHOLD_100 = 0.003;
    private static final double DEFAULT_THRESHOLD_100 = 0.001;

    // Constants for spending thresholds
    private static final double SPENDING_THRESHOLD_500 = 500;
    private static final double SPENDING_THRESHOLD_300 = 300;
    private static final double SPENDING_THRESHOLD_100 = 100;

    private final String plan;

    public SpendingThresholdCashback(final String plan) {
        this.plan = plan;
    }

    @Override
    public void applyCashback(final Commerciant commerciant,
                              final Account account,
                              final double amount,
                              final String currency,
                              final ExchangeRates exchangeRates) {
        double totalSpent = account.getAmountSpentOnSTCommerciants();
        double cashbackRate = 0.0;

        if (totalSpent >= SPENDING_THRESHOLD_500) {
            cashbackRate = switch (plan) {
                case "gold" -> GOLD_THRESHOLD_500;
                case "silver" -> SILVER_THRESHOLD_500;
                default -> DEFAULT_THRESHOLD_500;
            };
        } else if (totalSpent >= SPENDING_THRESHOLD_300) {
            cashbackRate = switch (plan) {
                case "gold" -> GOLD_THRESHOLD_300;
                case "silver" -> SILVER_THRESHOLD_300;
                default -> DEFAULT_THRESHOLD_300;
            };
        } else if (totalSpent >= SPENDING_THRESHOLD_100) {
            cashbackRate = switch (plan) {
                case "gold" -> GOLD_THRESHOLD_100;
                case "silver" -> SILVER_THRESHOLD_100;
                default -> DEFAULT_THRESHOLD_100;
            };
        }

        if (cashbackRate > 0) {
            account.addDiscount(new Discount(cashbackRate, "SpendingThreshold"));
        }
    }
}
