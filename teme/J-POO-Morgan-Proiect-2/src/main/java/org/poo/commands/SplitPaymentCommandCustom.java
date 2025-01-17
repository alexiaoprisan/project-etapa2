package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.splitPayment.SplitPayment;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.user.User;
import org.poo.user.UserRegistry;

import java.util.List;

/**
 * The SplitPaymentCommand class is a command that splits a
 * payment between multiple users.
 * The payment is split in a custom way, meaning that the user specifies the
 * amount that each user will receive.
 */
public final class SplitPaymentCommandCustom implements Command {
    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int timestamp;
    private final double totalAmount;
    private final String currency;
    private final List<String> accountsIBAN;
    private final ExchangeRates exchangeRates;
    private final List<Double> amountForEachAccount;
    private final SplitPaymentsRegistry splitPaymentsRegistry;


    /**
     * Constructor for the SplitPaymentCommand class.
     *
     * @param userRegistry  the UserRegistry
     * @param output        the output ArrayNode
     * @param timestamp     the timestamp of the command
     * @param totalAmount   the total amount to be split
     * @param currency      the currency of the total amount
     * @param accounts      the list of IBANs of the accounts
     * @param exchangeRates the ExchangeRates object
     */
    public SplitPaymentCommandCustom(final UserRegistry userRegistry,
                                     final ArrayNode output,
                                     final int timestamp,
                                     final double totalAmount,
                                     final String currency,
                                     final List<String> accounts,
                                     final ExchangeRates exchangeRates,
                                     final List<Double> amountForEachAccount,
                                     final SplitPaymentsRegistry splitPaymentsRegistry) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.accountsIBAN = accounts;
        this.exchangeRates = exchangeRates;
        this.amountForEachAccount = amountForEachAccount;
        this.splitPaymentsRegistry = splitPaymentsRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {

        // check if all accounts exist
        for (String accountIBAN : accountsIBAN) {
            Account account = userRegistry.getAccountByIBAN(accountIBAN);
            if (account == null) {
                return;
            }

            User user = userRegistry.getUserByIBAN(accountIBAN);
            if (user == null) {
                return;
            }

            if (account.getType().equals("business")) {
                return;
            }
        }

        // check if the sum of the amounts for each
        // account is equal to the total amount
        double sumForCheck = 0;
        for (double amount : amountForEachAccount) {
            sumForCheck += amount;
        }

        if (sumForCheck != totalAmount) {
            return;
        }

        // create the split payment to store in the split payments registry
        SplitPayment splitPayment = new SplitPayment();
        splitPayment.setCurrency(currency);
        splitPayment.setSplitPaymentType("custom");
        splitPayment.setTotalAmount(totalAmount);
        splitPayment.setTimestamp(timestamp);
        for (int i = 0; i < accountsIBAN.size(); i++) {
            String accountIBAN = accountsIBAN.get(i);
            splitPayment.addUserPayment(accountsIBAN.get(i),
                    amountForEachAccount.get(i),
                    userRegistry.getUserByIBAN(accountIBAN));
        }

        // add the split payment to the split payments registry
        splitPaymentsRegistry.addSplitPayment(splitPayment);

    }


}
