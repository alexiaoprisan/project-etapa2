package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.exchangeRates.ExchangeRates;

/**
 * The NrOfTransactionsCashback class is responsible for managing
 * the cashback strategy based on the number of transactions for
 * all the commerciants with the type nrOfTransactions.
 * In this strategy, the cashback is applied based on the number of transactions.
 */
public class NrOfTransactionsCashback implements CashbackStrategy {

    // Constants for cashback thresholds and values
    private static final int SECOND_TRANSACTION_THRESHOLD = 2;
    private static final int FIFTH_TRANSACTION_THRESHOLD = 5;
    private static final int TENTH_TRANSACTION_THRESHOLD = 10;

    private static final double SECOND_TRANSACTION_CASHBACK = 0.02;
    private static final double FIFTH_TRANSACTION_CASHBACK = 0.05;
    private static final double TENTH_TRANSACTION_CASHBACK = 0.1;

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyCashback(final Commerciant commerciant,
                              final Account account,
                              final double amount,
                              final String currency,
                              final ExchangeRates exchangeRates) {
        commerciant.incrementNrOfTransactions();
        int nrOfTransactions = commerciant.getNrOfTransactions();

        if (nrOfTransactions == SECOND_TRANSACTION_THRESHOLD) {
            account.addDiscount(new Discount(SECOND_TRANSACTION_CASHBACK, "Food"));
        } else if (nrOfTransactions == FIFTH_TRANSACTION_THRESHOLD) {
            account.addDiscount(new Discount(FIFTH_TRANSACTION_CASHBACK, "Clothes"));
        } else if (nrOfTransactions == TENTH_TRANSACTION_THRESHOLD) {
            account.addDiscount(new Discount(TENTH_TRANSACTION_CASHBACK, "Tech"));
        }
    }
}
