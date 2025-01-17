package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

/**
 * Strategy for applying discounts on clothes.
 */
public final class ClothesDiscountStrategy implements DiscountStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDiscount(final Account account,
                              final Commerciant commerciant,
                              final double amount) {
        Discount discount = account.getDiscountByType("Clothes");
        if (discount != null && !discount.checkIUsed()) {
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
        }
    }
}
