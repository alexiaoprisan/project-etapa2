package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public interface CashbackStrategy {
    double calculateCashback(double transactionValue, Account account, Commerciant commerciant);
}

