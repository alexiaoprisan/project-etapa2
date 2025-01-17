package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.exchangeRates.ExchangeRates;

/**
 * The CashbackStrategy interface is responsible for managing the cashback strategy.
 */
public interface CashbackStrategy {

    /**
     * Apply the cashback strategy.
     * @param commerciant The commerciant where the cashback is being applied.
     * @param account The account where the cashback is being applied.
     * @param amount The amount of the account to apply the cashback.
     * @param currency The currency of the cashback.
     * @param exchangeRates The exchange rates to convert the cashback to the account currency.
     */
    void applyCashback(Commerciant commerciant,
                       Account account,
                       double amount,
                       String currency,
                       ExchangeRates exchangeRates);
}


