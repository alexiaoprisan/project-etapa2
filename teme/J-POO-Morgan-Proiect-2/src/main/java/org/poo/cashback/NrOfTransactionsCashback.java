package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.exchangeRates.ExchangeRates;

public class NrOfTransactionsCashback implements CashbackStrategy {
    @Override
    public void applyCashback(Commerciant commerciant, Account account, double amount, String currency, ExchangeRates exchangeRates) {
        commerciant.incrementNrOfTransactions();
        int nrOfTransactions = commerciant.getNrOfTransactions();

        if (nrOfTransactions == 2) {
            account.addDiscount(new Discount(0.02, "Food"));
        } else if (nrOfTransactions == 5) {
            account.addDiscount(new Discount(0.05, "Clothes"));
        } else if (nrOfTransactions == 10) {
            account.addDiscount(new Discount(0.1, "Tech"));
        }
    }
}
