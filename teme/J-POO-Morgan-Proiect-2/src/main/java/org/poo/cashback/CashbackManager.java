package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.exchangeRates.ExchangeRates;

public class CashbackManager {
    private CashbackStrategy strategy;

    public void setStrategy(CashbackStrategy strategy) {
        this.strategy = strategy;
    }

    public void applyCashback(Commerciant commerciant, Account account, double amount, String currency, ExchangeRates exchangeRates) {
        if (strategy != null) {
            strategy.applyCashback(commerciant, account, amount, currency, exchangeRates);
        }
    }
}
