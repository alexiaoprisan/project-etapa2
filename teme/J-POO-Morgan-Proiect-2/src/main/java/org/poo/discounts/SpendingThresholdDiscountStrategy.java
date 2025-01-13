package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class SpendingThresholdDiscountStrategy implements DiscountStrategy {
    @Override
    public void applyDiscount(Account account, Commerciant commerciant, double amount) {
        Discount discount = account.getDiscountByType("SpendingThreshold");
        if (discount != null) {
            System.out.println("SpendingThreshold discount applied  " + discount.getValue() + " " + amount + " " + discount.getValue());
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
            account.removeDiscount(discount);
        }
    }
}
