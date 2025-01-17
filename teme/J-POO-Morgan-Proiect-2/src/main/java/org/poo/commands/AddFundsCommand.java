package org.poo.commands;

import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.user.UserRegistry;
import org.poo.user.User;

/**
 * Command to add funds to an account.
 * It will search for the account with the given iban and add the amount to its balance.
 */
public final class AddFundsCommand implements Command {
    private final UserRegistry userRegistry;
    private final String iban;
    private final double amount;
    private final String email;

    /**
     * Constructor for the AddFundsCommand.
     *
     * @param userRegistry the user registry
     * @param iban         the iban of the account
     * @param amount       the amount to add
     */
    public AddFundsCommand(final UserRegistry userRegistry,
                           final String iban,
                           final double amount,
                           final String email) {
        this.userRegistry = userRegistry;
        this.iban = iban;
        this.amount = amount;
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

        Account account = userRegistry.getAccountByIBAN(iban);
        if (account == null) {
            return;
        }

        if (account.getType().equals("business")) {


            BusinessAccount businessAccount = (BusinessAccount) account;

            User owner = businessAccount.getOwner();

            if (!businessAccount.isManager(user) && !businessAccount.isEmployee(user)
                    && !owner.equals(user)) {
                return;
            }

            if (businessAccount.isEmployee(user)) {
                if (amount > businessAccount.getMaxDepositedLimit()) {
                    return;
                }
                businessAccount.addEmployeeDepositedAmount(user, amount);
                businessAccount.setTotalDeposited(businessAccount.getTotalDeposited() + amount);

            } else if (businessAccount.isManager(user)) {
                businessAccount.addManagerDepositedAmount(user, amount);
                businessAccount.setTotalDeposited(businessAccount.getTotalDeposited() + amount);
            }
        }

        account.setBalance(account.getBalance() + amount);

    }

}
