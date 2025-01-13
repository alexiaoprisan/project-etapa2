package org.poo.commands;

import org.poo.commerciants.CommerciantRegistry;
import org.poo.splitPayment.SplitPaymentsRegistry;
import org.poo.user.UserRegistry;
import org.poo.fileio.CommandInput;
import org.poo.exchangeRates.ExchangeRates;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Factory class for creating Command objects.
 * The CommandFactory class is responsible for creating Command objects based on the command name.
 * <p>
 * This class is part of the Command design pattern.
 */
public final class CommandFactory {
    private final UserRegistry userRegistry;
    private final ExchangeRates exchangeRates;
    private final ArrayNode output;
    private final CommerciantRegistry commerciantRegistry;
    private final SplitPaymentsRegistry splitPaymentsRegistry;

    /**
     * Constructor for the CommandFactory class.
     *
     * @param userRegistry  the UserRegistry object
     * @param output        the ArrayNode object
     * @param exchangeRates the ExchangeRates object
     */
    public CommandFactory(final UserRegistry userRegistry,
                          final ArrayNode output,
                          final ExchangeRates exchangeRates,
                          final CommerciantRegistry commerciantRegistry,
                          final SplitPaymentsRegistry splitPaymentsRegistry) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.exchangeRates = exchangeRates;
        this.commerciantRegistry = commerciantRegistry;
        this.splitPaymentsRegistry = splitPaymentsRegistry;
    }

    /**
     * Creates a Command object based on the command type.
     *
     * @param commandType the command name
     * @param input       the CommandInput object
     * @return the Command object
     */
    public Command createCommand(final String commandType,
                                 final CommandInput input) {
        int timestamp = input.getTimestamp();
        switch (commandType) {
            case "printUsers":
                return new PrintUsersCommand(userRegistry, output, timestamp);

            case "addAccount":
                return new AddAccountCommand(userRegistry, timestamp,
                        input.getEmail(), input.getAccountType(), input.getCurrency(), input.getInterestRate(),
                        exchangeRates);

            case "createCard":
                return new CreateCardCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getAccount(), "createCard");

            case "createOneTimeCard":
                return new CreateCardCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getAccount(), "createOneTimeCard");

            case "addFunds":
                return new AddFundsCommand(userRegistry, input.getAccount(), input.getAmount(),
                        input.getEmail());

            case "deleteAccount":
                return new DeleteAccountCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getAccount());

            case "deleteCard":
                return new DeleteCardCommand(userRegistry, timestamp,
                        input.getEmail(), input.getCardNumber());

            case "payOnline":
                return new PayOnlineCommand(userRegistry, exchangeRates, output, timestamp,
                        input.getCardNumber(), input.getAmount(), input.getCurrency(),
                        input.getDescription(), input.getCommerciant(), input.getEmail(),
                        commerciantRegistry);

            case "sendMoney":
                return new SendMoneyCommand(userRegistry, output, timestamp,
                        input.getAccount(), input.getReceiver(), input.getAmount(),
                        input.getEmail(), input.getDescription(), exchangeRates, commerciantRegistry);

            case "printTransactions":
                return new PrintTransactionsCommand(userRegistry, output, timestamp,
                        input.getEmail());
//
//            case "setAlias":
//                return new SetAliasCommand(userRegistry, timestamp,
//                        input.getEmail(), input.getAccount(), input.getAlias());
//
            case "checkCardStatus":
                return new CheckCardStatusCommand(userRegistry, output,
                        input.getCardNumber(), timestamp);

            case "setMinimumBalance":
                return new SetMinimumBalanceCommand(userRegistry, timestamp,
                        input.getAccount(), input.getAmount());

            case "splitPayment":
                if (input.getSplitPaymentType().equals("custom"))
                    return new SplitPaymentCommandCustom(userRegistry, output, timestamp,
                            input.getAmount(), input.getCurrency(), input.getAccounts(), exchangeRates,
                            input.getAmountForUsers(), splitPaymentsRegistry);
                else
                    return new SplitPaymentCommandEqual(userRegistry, output, timestamp,
                        input.getAmount(), input.getCurrency(), input.getAccounts(), exchangeRates,
                            splitPaymentsRegistry);

            case "acceptSplitPayment":
                return new AcceptSplitPaymentCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getSplitPaymentType(), splitPaymentsRegistry,
                        exchangeRates);

            case "rejectSplitPayment":
                return new RejectionSplitPaymentCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getSplitPaymentType(), splitPaymentsRegistry, exchangeRates);

            case "changeInterestRate":
                return new ChangeInterestRateCommand(userRegistry, output, timestamp,
                        input.getAccount(), input.getInterestRate());

            case "addInterest":
                return new AddInterestCommand(userRegistry, output, timestamp,
                        input.getAccount());

            case "withdrawSavings":
                return new WithdrawSavingsCommand(userRegistry, output, timestamp, exchangeRates,
                        input.getAccount(), input.getAmount(), input.getCurrency());

            case "upgradePlan":
                return new UpgradePlanCommand(userRegistry, output, timestamp, input.getAccount(),
                        input.getNewPlanType(), exchangeRates);

            case "cashWithdrawal":
                return new CashWithdrawalCommand(userRegistry, output, timestamp, exchangeRates,
                        input.getCardNumber(), input.getAmount(), input.getLocation(), input.getEmail());

            case "report":
                return new ReportCommand(userRegistry, output, input.getStartTimestamp(),
                        input.getEndTimestamp(),
                        input.getAccount(), timestamp);

            case "spendingsReport":
                return new SpendingsReportCommand(userRegistry, output,
                        input.getStartTimestamp(), input.getEndTimestamp(),
                        input.getAccount(), timestamp);

            case "addNewBusinessAssociate":
                return new AddNewBusinessAssociateCommand(userRegistry, output, timestamp,
                        input.getEmail(), input.getAccount(), input.getRole());

            case "changeSpendingLimit":
                return new ChangeSpendingLimitCommand(userRegistry, output, timestamp,
                        input.getAccount(), input.getEmail(), input.getAmount());

            case "businessReport":
                return new BusinessReportCommand(userRegistry, output,
                        input.getStartTimestamp(), input.getEndTimestamp(),
                        input.getAccount(), input.getType(), timestamp);

            case "changeDepositLimit":
                return new ChangeDepositLimitCommand(userRegistry, output, timestamp,
                        input.getAccount(), input.getEmail(), input.getAmount());

            default:
                // Log unknown command type and skip
                return null;
        }

    }
}
