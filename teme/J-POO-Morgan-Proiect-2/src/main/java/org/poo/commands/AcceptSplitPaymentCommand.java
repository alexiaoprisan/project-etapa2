package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.user.User;
import org.poo.user.UserRegistry;

import java.util.List;

public class AcceptSplitPaymentCommand implements Command {
    private UserRegistry userRegistry;
    private ArrayNode output;
    private int timestamp;
    private String email;
    private String splitPaymentType;

    public AcceptSplitPaymentCommand(final UserRegistry userRegistry,
                                     final ArrayNode output,
                                     final int timestamp,
                                     final String email,
                                     final String splitPaymentType) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.timestamp = timestamp;
        this.email = email;
        this.splitPaymentType = splitPaymentType;
    }


    @Override
    public void execute() {
        User user = userRegistry.getUserByEmail(email);
        if (user == null) {
            return;
        }

        user.acceptSplitPayment(splitPaymentType);
    }
}