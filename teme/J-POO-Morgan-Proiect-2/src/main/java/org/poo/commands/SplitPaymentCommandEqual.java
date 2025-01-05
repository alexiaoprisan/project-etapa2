package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.splitPayment.SplitPayment;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.user.UserRegistry;
import org.poo.user.User;

import java.util.List;

/**
 * Command to split a payment between multiple accounts.
 * Checks if the accounts exist and have enough balance to perform the split payment.
 */
public final class SplitPaymentCommandEqual implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private double totalAmount;
    private String currency;
    private List<String> accountsIBAN;
    private ExchangeRates exchangeRates;
    private SplitPaymentsRegistry splitPaymentsRegistry;


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
    public SplitPaymentCommandEqual(final UserRegistry userRegistry,
                                    final ArrayNode output,
                                    final int timestamp,
                                    final double totalAmount,
                                    final String currency,
                                    final List<String> accounts,
                                    final ExchangeRates exchangeRates,
                                    final SplitPaymentsRegistry splitPaymentsRegistry) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.accountsIBAN = accounts;
        this.exchangeRates = exchangeRates;
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
        }

        double amountForEachAccount = totalAmount / accountsIBAN.size();


        // create the split payment to store in the split payments registry
        SplitPayment splitPayment = new SplitPayment();
        splitPayment.setCurrency(currency);
        splitPayment.setSplitPaymentType("equal");
        splitPayment.setTotalAmount(totalAmount);
        splitPayment.setTimestamp(timestamp);
        for (int i = 0; i < accountsIBAN.size(); i++) {
            String accountIBAN = accountsIBAN.get(i);
            splitPayment.addUserPayment(accountsIBAN.get(i), amountForEachAccount, userRegistry.getUserByIBAN(accountIBAN));
        }

        // add the split payment to the split payments registry
        splitPaymentsRegistry.addSplitPayment(splitPayment);
    }

}
