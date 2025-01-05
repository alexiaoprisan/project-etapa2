package org.poo.bankingApp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Command;
import org.poo.commands.CommandFactory;
import org.poo.commerciants.Commerciant;
import org.poo.commerciants.CommerciantRegistry;
import org.poo.exchangeRates.ExchangeInputFormat;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.*;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.user.User;
import org.poo.user.UserRegistry;
import org.poo.utils.Utils;

public final class BankManager {

    /**
     * Process the commands from the input data
     *
     * @param inputData the input data to process
     * @param output    the output node to write the results to
     */
    public void processCommands(final ObjectInput inputData, final ArrayNode output) {
        // create a new instance of the exchange rates, singleton pattern
        ExchangeRates exchangeRates = ExchangeRates.getInstance();

        // clear the exchange rates
        exchangeRates.reset();

        // create a new instance of the commerciants registry, singleton pattern
        CommerciantRegistry commerciantRegistry = CommerciantRegistry.getInstance();

        // clear the commerciants registry
        commerciantRegistry.reset();

        // holds all the users
        UserRegistry userRegistry = UserRegistry.getInstance();

        // clear the user registry
        userRegistry.reset();

        // holds all the split payments
        SplitPaymentsRegistry splitPaymentsRegistry = SplitPaymentsRegistry.getInstance();

        // clear the split payments registry
        splitPaymentsRegistry.reset();

        // reset the random seed for account IBAN and card number generation
        Utils.resetRandom();

        // add all the users to the user registry
        processUsers(inputData);

        // add all the commerciants to the commerciants registry
        processCommerciants(inputData);

        // save the exchange rates to the exchange rates registry
        processExchageRates(inputData);

        // try to find new exchange rates based on the existing ones
        exchangeRates.findNewExchangeRates();

        CommandFactory commandFactory = new CommandFactory(userRegistry, output, exchangeRates,
                commerciantRegistry, splitPaymentsRegistry);
        for (CommandInput input : inputData.getCommands()) {
            String commandType = input.getCommand();

            // create the command using the factory
            Command command = commandFactory.createCommand(commandType, input);
            if (command == null) {
                continue; // Skip this iteration if the command is null
            }
            command.execute();
        }

    }

    /**
     * Process the users from the input data and save them to the user registry
     *
     * @param inputData the input data to process
     */
    public void processUsers(final ObjectInput inputData) {
        UserRegistry userRegistry = UserRegistry.getInstance();
        for (UserInput user : inputData.getUsers()) {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getBirthDate(), user.getOccupation());

            if (user.getOccupation().equals("student")) {
                newUser.setServicePlan("student");
            }
            else {
                newUser.setServicePlan("standard");
            }
            userRegistry.addUser(newUser);
        }
    }

    /**
     * Process the exchange rates from the input data and save them
     * to the exchange rates registry
     *
     * @param inputData the input data to process
     */
    public void processExchageRates(final ObjectInput inputData) {
        ExchangeRates exchangeRates = ExchangeRates.getInstance();

        for (ExchangeInput exchange : inputData.getExchangeRates()) {
            ExchangeInputFormat exchangeInputData = new ExchangeInputFormat();
            exchangeInputData.setFrom(exchange.getFrom());
            exchangeInputData.setTo(exchange.getTo());
            exchangeInputData.setRate(exchange.getRate());
            exchangeInputData.setTimestamp(exchange.getTimestamp());

            exchangeRates.addExchangeRate(exchangeInputData);
        }
    }

    public void processCommerciants(final ObjectInput inputData) {
        CommerciantRegistry commerciantRegistry = CommerciantRegistry.getInstance();
        for (CommerciantInput commerciant : inputData.getCommerciants()) {
            Commerciant newCommerciant = new Commerciant(commerciant.getCommerciant(), commerciant.getId(),
                    commerciant.getAccount(), commerciant.getType(), commerciant.getCashbackStrategy());

            commerciantRegistry.addCommerciant(newCommerciant);
        }
    }
}
