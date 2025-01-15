package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.exchangeRates.ExchangeRates;

public class SpendingThresholdCashback implements CashbackStrategy {
    private final String plan;

    public SpendingThresholdCashback(String plan) {
        this.plan = plan;
    }

    @Override
    public void applyCashback(Commerciant commerciant, Account account, double amount, String currency, ExchangeRates exchangeRates) {
        //account.addAmountSpentOnSTCommerciants(amount);
        double totalSpent = account.getAmountSpentOnSTCommerciants();

//        // we need to transform this sum in RON
//        if (!currency.equals("RON")) {
//            double rate = exchangeRates.convertExchangeRate(currency, "RON");
//            totalSpent *= rate;
//        }
        double cashbackRate = 0.0;
        if (totalSpent >= 500) {
            cashbackRate = switch (plan) {
                case "gold" -> 0.007;
                case "silver" -> 0.005;
                default -> 0.0025;
            };
        } else if (totalSpent >= 300) {
            cashbackRate = switch (plan) {
                case "gold" -> 0.0055;
                case "silver" -> 0.004;
                default -> 0.002;
            };
        } else if (totalSpent >= 100) {
            cashbackRate = switch (plan) {
                case "gold" -> 0.005;
                case "silver" -> 0.003;
                default -> 0.001;
            };
        }

        if (cashbackRate > 0) {
            account.addDiscount(new Discount(cashbackRate, "SpendingThreshold"));
        }
    }
}
