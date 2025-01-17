package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.CashWithdrawalError;
import org.poo.transaction.CashWithdrawalTransaction;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import org.poo.user.UserRegistry;

/**
 * CashWithdrawalCommand class is a command that executes a cash withdrawal operation.
 */
public final class CashWithdrawalCommand implements Command {
    private final UserRegistry userRegistry;
    private final int timestamp;
    private final String cardNumber;
    private final double amount;
    private final String location;
    private final String email;
    private final ExchangeRates exchangeRates;
    private final ArrayNode output;

    /**
     * Constructor for the CashWithdrawalCommand class.
     *
     * @param userRegistry  the user registry
     * @param output        the output array
     * @param timestamp     the timestamp
     * @param exchangeRates the exchange rates
     * @param cardNumber    the card number
     * @param amount        the amount
     * @param location      the location
     * @param email         the email
     */
    public CashWithdrawalCommand(final UserRegistry userRegistry,
                                 final ArrayNode output,
                                 final int timestamp,
                                 final ExchangeRates exchangeRates,
                                 final String cardNumber,
                                 final double amount,
                                 final String location,
                                 final String email) {
        this.userRegistry = userRegistry;
        this.timestamp = timestamp;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.location = location;
        this.email = email;
        this.exchangeRates = exchangeRates;
        this.output = output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            ObjectNode node = output.addObject();
            node.put("command", "cashWithdrawal");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "User not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        Account account = user.getAccountByCardNumber(cardNumber);
        if (account == null) {
            ObjectNode node = output.addObject();
            node.put("command", "cashWithdrawal");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        Card card = account.getCardByNumber(cardNumber);
        if (card == null) {
            ObjectNode node = output.addObject();
            node.put("command", "cashWithdrawal");
            ObjectNode outputNode = node.putObject("output");
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", timestamp);
            node.put("timestamp", timestamp);
            return;
        }

        double exchangeRate = exchangeRates.convertExchangeRate("RON", account.getCurrency());
        double convertedSum = amount * exchangeRate;

        double amountToPay = user.addCommission(convertedSum, exchangeRates, account.getCurrency());

        if (account.getBalance() < amountToPay) {
            Transaction transaction = new CashWithdrawalError(timestamp, "Insufficient funds");
            user.addTransaction(transaction);
            return;
        }

        if (card.getStatus().equals("frozen")) {
            Transaction transaction = new CashWithdrawalError(timestamp, "The card is frozen");
            user.addTransaction(transaction);
            return;
        }

        if (account.getBalance() - amountToPay < account.getMinBalance()) {
            Transaction transaction = new CashWithdrawalError(timestamp,
                    "Cannot perform payment due to a minimum balance being set.");
            user.addTransaction(transaction);
            return;
        }

        account.setBalance(account.getBalance() - amountToPay);

        Transaction transaction = new CashWithdrawalTransaction(timestamp,
                "Cash withdrawal of " + amount, amount);
        user.addTransaction(transaction);

    }
}
