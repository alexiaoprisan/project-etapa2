package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class FoodDiscountStrategy implements DiscountStrategy {
    @Override
    public void applyDiscount(Account account, Commerciant commerciant, double amount) {
        Discount discount = account.getDiscountByType("Food");
        if (discount != null && !discount.isUsed()) {
            System.out.println("Food discount applied  " + account.getIBAN());
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
        }
    }
}
