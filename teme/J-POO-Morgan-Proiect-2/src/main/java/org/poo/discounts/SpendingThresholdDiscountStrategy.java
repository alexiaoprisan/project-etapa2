package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class SpendingThresholdDiscountStrategy implements DiscountStrategy {
    @Override
    public void applyDiscount(Account account, Commerciant commerciant, double amount) {
        Discount discount = account.getDiscountByType("SpendingThreshold");
        if (discount != null && !discount.isUsed()) {
            System.out.println("SpendingThreshold discount applied  " + account.getIBAN());
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
        }
    }
}
