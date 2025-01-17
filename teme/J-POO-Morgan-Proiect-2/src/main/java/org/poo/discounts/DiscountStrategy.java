package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

/**
 * Interface that represents a discount strategy.
 */
public interface DiscountStrategy {

    /**
     * Applies a discount to the given amount based on the account and commerciant.
     *
     * @param account The account to apply the discount to.
     * @param commerciant The commerciant to apply the discount from.
     * @param amount The amount to apply the discount to.
     */
    void applyDiscount(Account account, Commerciant commerciant, double amount);
}
