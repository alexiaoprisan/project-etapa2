package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.ClassicAccount;
import org.poo.user.UserRegistry;
import org.poo.commerciants.Commerciant;
import org.poo.report.ClassicReport;
import org.poo.report.PaymentsRecord;
import org.poo.user.User;
import org.poo.card.Card;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.Transaction;
import org.poo.transaction.CardPaymentTransaction;
import org.poo.transaction.CardDestroyed;
import org.poo.transaction.FrozenCard;
import org.poo.transaction.InsufficientFunds;
import org.poo.transaction.NewCardCreatedTransaction;
import org.poo.transaction.WarningForPay;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utils.Utils;

/**
 * Command class for the payOnline command.
 * Is is used to pay online using a card, if the card has enough funds.
 */
public final class PayOnlineCommand implements Command {
    private final UserRegistry userRegistry;
    private final ExchangeRates exchangeRates;
    private final ArrayNode output;
    private final int timestamp;
    private final String cardNumber;
    private double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;
    private static final int MIN_DIFFERENCE = 30;

    /**
     * Constructor for the PayOnlineCommand class.
     *
     * @param userRegistry  the UserRegistry object
     * @param exchangeRates the ExchangeRates object
     * @param output        the ArrayNode object
     * @param timestamp     the timestamp
     * @param cardNumber    the card number
     * @param amount        the amount to pay
     * @param currency      the currency
     * @param description   the description
     * @param commerciant   the commerciant
     * @param email         the email
     */
    public PayOnlineCommand(final UserRegistry userRegistry,
                            final ExchangeRates exchangeRates,
                            final ArrayNode output,
                            final int timestamp,
                            final String cardNumber,
                            final double amount, final String currency,
                            final String description, final String commerciant,
                            final String email) {
        this.userRegistry = userRegistry;
        this.exchangeRates = exchangeRates;
        this.output = output;
        this.timestamp = timestamp;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            return;
        }

        // auxiliar variable to check if the card was found
        int foundCard = 0;

        for (Account account : user.getAccounts()) {
            Card card = account.getCardByNumber(cardNumber);
            if (card != null) {
                // the card was found
                foundCard = 1;

                String cardCurrency = account.getCurrency();

                // check if the card currency is different from the payment currency
                if (!cardCurrency.equals(currency)) {
                    double rate = exchangeRates.convertExchangeRate(cardCurrency, currency);
                    // check if the exchange rate was found
                    if (rate != 0) {
                        // calculate the amount in the card currency
                        amount = amount / rate;
                    }
                }

                // check if the card is frozen
                if (card.getStatus().equals("frozen")) {
                    // create a transaction for the frozen card
                    // the card cannot be used to make payments
                    Transaction transaction = new FrozenCard(timestamp,
                            "The card is frozen");
                    user.addTransaction(transaction);
                    return;
                }

                double amountToPay = user.addCommission(amount);

                // check if the account has enough funds
                if (account.getBalance() >= amountToPay) {

                    // freeze the card if the balance is less than the minimum balance
                    if (account.getBalance() - amountToPay <= account.getMinBalance()) {
                        card.setStatus("frozen");

                        // create a transaction to warn the user that the card will be frozen
                        Transaction transactionErorr = new WarningForPay(timestamp,
                                "You have reached the minimum amount "
                                        + "of funds, the card will be frozen");
                        user.addTransaction(transactionErorr);
                        return;
                    }

                    // make the card status "warning" if the balance is less than the
                    // minimum balance + 30
//                    if (account.getBalance() - account.getMinBalance() - amountToPay < MIN_DIFFERENCE) {
//
//                        // create a transaction to warn the user that the card will be frozen
//                        Transaction transactionErorr = new WarningForPay(timestamp,
//                                "You have reached the minimum amount "
//                                        + "of funds, the card will be frozen");
//                        user.addTransaction(transactionErorr);
//                        card.setStatus("warning");
//
//                    }

                    // if the cases above are not true, the payment can be made
                    account.setBalance(account.getBalance() - amountToPay);



                    // create a transaction for the payment
                    Transaction transaction = new CardPaymentTransaction(timestamp,
                            "Card payment", amount, commerciant);
                    user.addTransaction(transaction);

                    Commerciant newCommerciant = new Commerciant(commerciant,
                            amount, timestamp);

                    // verify if the account is a classic account
                    // in order to save the transaction for the report and the spending report
                    if (account.getType().equals("classic")) {
                        // save the commerciant in the account

                        // add the transaction to the report and the payments record
                        // (for the spending report)
                        ClassicAccount classicAccount = (ClassicAccount) account;
                        ClassicReport report = classicAccount.getReport();
                        PaymentsRecord paymentsRecord = classicAccount.getPaymentsRecord();

                        report.addTransaction(transaction);
                        paymentsRecord.addTransaction(transaction);

                        // add the commerciant to the list of commerciants in the account
                        classicAccount.addCommerciant(newCommerciant);
                    }

                    // check if the card is a oneTimePay card
                    if (card.getType().equals("oneTimePay")) {
                        // the card will be used only once, then it will be destroyed
                        // and a new card will be created

                        // generate a new card number
                        String newCardNumber = Utils.generateCardNumber();
                        card.setCardNumber(newCardNumber);

                        // create a transaction for the destroyed card and the new card
                        Transaction transactionCardDestroyed = new CardDestroyed(timestamp,
                                "The card has been destroyed", account.getIBAN(),
                                cardNumber, user.getEmail());
                        user.addTransaction(transactionCardDestroyed);

                        Transaction transactionNewCard = new NewCardCreatedTransaction(timestamp,
                                "New card created", account.getIBAN(),
                                newCardNumber, user.getEmail());
                        user.addTransaction(transactionNewCard);
                    }

                } else {
                    // if the payment cannot be made, create a transaction for insufficient funds
                    Transaction transaction = new InsufficientFunds(timestamp,
                            "Insufficient funds");
                    user.addTransaction(transaction);
                }

            }
        }

        // if the card was not found, print an error message
        if (foundCard == 0) {
            ObjectNode outputNode = output.addObject();
            outputNode.put("command", "payOnline");
            ObjectNode outputObject = outputNode.putObject("output");
            outputObject.put("description", "Card not found");
            outputObject.put("timestamp", timestamp);
            outputNode.put("timestamp", timestamp);
        }

    }
}
