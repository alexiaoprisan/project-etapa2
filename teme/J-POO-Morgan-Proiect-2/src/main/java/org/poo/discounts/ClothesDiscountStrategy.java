package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class ClothesDiscountStrategy implements DiscountStrategy {
    @Override
    public void applyDiscount(Account account, Commerciant commerciant, double amount) {
        Discount discount = account.getDiscountByType("Clothes");
        if (discount != null && !discount.isUsed()) {
            System.out.println("Clothes discount applied  " + account.getIBAN());
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
        }
    }
}
