package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.splitPayment.SplitPayment;
import org.poo.splitPayment.SplitPaymentsRegistry;
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
            return;
        }

        // set the split payment as rejected
        user.rejectSplitPayment(splitPaymentType);

        // find the split payment in the registry to delete it
        SplitPayment splitPayment = splitPaymentsRegistry.getSplitPaymentByUserEmail(email);
        if (splitPayment == null) {
            return;
        }

        List<User> usersInvolvedInSplitPayment = splitPayment.getUsers();
        for (User userInvolved : usersInvolvedInSplitPayment) {
            // set the split payment as uninitialized for all users involved
            userInvolved.setAcceptEqualSplitPayment(0);

            // afisare one user has rejected the payment
        }

        splitPaymentsRegistry.removeSplitPayment(splitPayment);
    }
}