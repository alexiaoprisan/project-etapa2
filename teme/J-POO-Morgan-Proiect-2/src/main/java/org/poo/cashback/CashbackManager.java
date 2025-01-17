package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.exchangeRates.ExchangeRates;

/**
 * The CashbackManager class is responsible for managing the cashback strategy.
 * Uses the Strategy design pattern.
 * The strategy is set by the setStrategy method.
 * The applyCashback method applies the cashback strategy.
 */
public class CashbackManager {
    private CashbackStrategy strategy;

    /**
     * Set the cashback strategy.
     * @param strategy The cashback strategy to be set.
     */
    public void setStrategy(final CashbackStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Apply the cashback strategy.
     * @param commerciant The commerciant where the cashback is being applied.
     * @param account The account where the cashback is being applied.
     * @param amount The amount of the account to apply the cashback.
     * @param currency The currency of the cashback.
     * @param exchangeRates The exchange rates to convert the cashback to the account currency.
     */
    public void applyCashback(final Commerciant commerciant,
                              final Account account,
                              final double amount,
                              final String currency,
                              final ExchangeRates exchangeRates) {
        if (strategy != null) {
            strategy.applyCashback(commerciant, account, amount, currency, exchangeRates);
        }
    }
}
