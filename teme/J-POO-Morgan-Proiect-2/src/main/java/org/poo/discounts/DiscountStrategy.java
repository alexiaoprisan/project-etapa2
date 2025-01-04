package org.poo.discounts;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public interface DiscountStrategy {
    void applyDiscount(Account account, Commerciant commerciant, double amount);
}
