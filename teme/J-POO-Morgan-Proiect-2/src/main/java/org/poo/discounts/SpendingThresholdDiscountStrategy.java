package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

/**
 * Strategy for applying a discount based on the amount spent by the user.
 * This strategy is used when the user has a discount of type "SpendingThreshold".
 */
public class SpendingThresholdDiscountStrategy implements DiscountStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDiscount(final Account account,
                              final Commerciant commerciant,
                              final double amount) {
        Discount discount = account.getDiscountByType("SpendingThreshold");
        if (discount != null) {
            account.setBalance(account.getBalance() + discount.getValue() * amount);
            discount.setUsed(true);
            account.removeDiscount(discount);
        }
    }
}
