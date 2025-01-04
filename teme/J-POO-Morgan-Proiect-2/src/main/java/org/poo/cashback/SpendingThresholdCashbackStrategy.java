package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class SpendingThresholdCashbackStrategy implements CashbackStrategy {
    @Override
    public double calculateCashback(double transactionValue, Account account, Commerciant commerciant) {
//        double totalSpending = account.getTotalSpendingForCategory(merchant.getType());
//        Plan plan = account.getPlan();
//
//        if (totalSpending >= 500) {
//            return transactionValue * plan.getCashbackPercentage(500);
//        } else if (totalSpending >= 300) {
//            return transactionValue * plan.getCashbackPercentage(300);
//        } else if (totalSpending >= 100) {
//            return transactionValue * plan.getCashbackPercentage(100);
//        }
        return 0;
    }
}
