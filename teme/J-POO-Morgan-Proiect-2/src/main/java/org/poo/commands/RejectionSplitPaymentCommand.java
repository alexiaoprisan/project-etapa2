package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.splitPayment.SplitPayment;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.transaction.SplitPaymentCustomReject;
import org.poo.transaction.Transaction;
import org.poo.user.User;
import org.poo.user.UserRegistry;

import java.util.List;

public class RejectionSplitPaymentCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private String email;
    private String splitPaymentType;
    private SplitPaymentsRegistry splitPaymentsRegistry;
    private ExchangeRates exchangeRates;

    public RejectionSplitPaymentCommand(final UserRegistry userRegistry,
                                     final ArrayNode output,
                                     final int timestamp,
                                     final String email,
                                     final String splitPaymentType,
                                     final SplitPaymentsRegistry splitPaymentsRegistry,
                                     final ExchangeRates exchangeRates) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.splitPaymentType = splitPaymentType;
        this.splitPaymentsRegistry = splitPaymentsRegistry;
        this.exchangeRates = exchangeRates;
    }


    @Override
    public void execute() {
        // find the user by email
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            ObjectNode error = output.addObject();
            error.put("command", "rejectSplitPayment");
            ObjectNode outputNode = error.putObject("output");
            outputNode.put("description", "User not found");
            outputNode.put("timestamp", timestamp);
            error.put("timestamp", timestamp);
            return;
        }

        // set the split payment as rejected
        user.rejectSplitPayment(splitPaymentType);

        // find the split payment in the registry to delete it
        SplitPayment splitPayment = splitPaymentsRegistry.getSplitPaymentByUserEmail(email, splitPaymentType);
        if (splitPayment == null) {
            return;
        }

        List<User> usersInvolvedInSplitPayment = splitPayment.getUsers();
        for (User userInvolved : usersInvolvedInSplitPayment) {
            // set the split payment as uninitialized for all users involved
            userInvolved.setAcceptEqualSplitPayment(0);

            String description = String.format("Split payment of %.2f %s",
                    splitPayment.getTotalAmount(), splitPayment.getCurrency());

            // print the transaction for each user involved
            Transaction transaction = new SplitPaymentCustomReject(splitPayment.getTimestamp(), description, splitPayment.getAmountForEachAccount(), splitPayment.getCurrency(), splitPayment.getAccountsIBAN(), splitPaymentType);
            userInvolved.addTransaction(transaction);
        }

        splitPaymentsRegistry.removeSplitPayment(splitPayment);
    }
}