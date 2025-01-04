package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class CashbackContext {
    private CashbackStrategy strategy;

    public void setStrategy(CashbackStrategy strategy) {
        this.strategy = strategy;
    }

    public double applyCashback(double transactionValue, Account account, Commerciant commerciant) {
        return strategy.calculateCashback(transactionValue, account, commerciant);
    }
}
