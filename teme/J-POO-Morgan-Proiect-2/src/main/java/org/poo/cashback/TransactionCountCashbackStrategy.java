package org.poo.cashback;

import org.poo.account.Account;
import org.poo.commerciants.Commerciant;

public class TransactionCountCashbackStrategy implements CashbackStrategy {
    @Override
    public double calculateCashback(double transactionValue, Account account, Commerciant commerciant) {
//        int transactions = account.getTransactionCountForMerchant(merchant);
//        if (transactions == 2 && merchant.getType() == MerchantType.FOOD) {
//            return transactionValue * 0.02;
//        } else if (transactions == 5 && merchant.getType() == MerchantType.CLOTHES) {
//            return transactionValue * 0.05;
//        } else if (transactions == 10 && commerciant.getType() == MerchantType.TECH) {
//            return transactionValue * 0.10;
//        }
        return 0;
    }
}

