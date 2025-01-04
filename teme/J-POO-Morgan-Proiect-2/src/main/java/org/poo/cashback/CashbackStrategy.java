package org.poo.cashback;

import org.poo.account.Account;
import org.poo.account.ClassicAccount;
import org.poo.commerciants.Commerciant;
import org.poo.exchangeRates.ExchangeRates;

public interface CashbackStrategy {
    void applyCashback(Commerciant commerciant, Account account, double amount, String currency, ExchangeRates exchangeRates);
}


