package org.poo.user;

import org.poo.account.Account;
import org.poo.account.AccountFactory;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.transaction.TransactionReport;
import org.poo.transaction.Transaction;
import org.poo.transaction.UpgradePlanTransaction;
import org.poo.transaction.UpgradePlanError;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a user.
 * The user can have multiple accounts and each account can have multiple cards.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String occupation;

    // service plan: can be standard, student, silver, gold
    private String servicePlan;

    // the list of accounts owned by the user
    private ArrayList<Account> accounts = new ArrayList<>();
    private boolean hasAccount = false;

    // the transaction report for the user
    private TransactionReport transactionReport = new TransactionReport();

    // 0 means the user did not decide yet, 1 means the user accepts custom split payment, 2 means the user did not accept custom split payment
    private int acceptCustomSplitPayment = 0;
    private int acceptEqualSplitPayment = 0;

    // how many times a user has made payments over 300 RON
    // if he makes more than 5, he will be upgraded to gold
    private int paymentsOverThreeHundred = 0;

    /**
     * Constructs a User instance with the specified details.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     */
    public User(final String firstName, final String lastName, final String email,
                final String birthDate, final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.occupation = occupation;
    }

    public User(User user) {
    }

    /**
     * Gets the user's first name.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the new first name of the user
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the new last name of the user
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's email address.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email address of the user
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets the user's birth date.
     *
     * @return the birth date of the user
     */
    public String getBirthDate() {
        return this.birthDate;
    }

    /**
     * Sets the user's birth date.
     *
     * @param birthDate the new birth date of the user
     */
    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the user's occupation.
     *
     * @return the occupation of the user
     */
    public String getOccupation() {
        return this.occupation;
    }

    /**
     * Sets the user's occupation.
     *
     * @param occupation the new occupation of the user
     */
    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    /**
     * Checks if the user has an account.
     *
     * @return true if the user has an account, false otherwise
     */
    public boolean userHasAccount() {
        return hasAccount;
    }

    /**
     * Gets the list of accounts owned by the user.
     *
     * @return the list of accounts
     */
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public int getPaymentsOverThreeHundred() {
        return paymentsOverThreeHundred;
    }

    public void incrementPaymentsOverThreeHundred() {
        paymentsOverThreeHundred++;
    }

    /**
     * Adds a new account for the user.
     *
     * @param accountType the type of the account
     * @param currency    the currency of the account
     * @param iban        the IBAN of the account
     */
    public void addAccount(final String accountType, final String currency, final String iban, double interestRate, User owner, double businessAmount) {
        // create a new account based on the account type, using the factory pattern
        AccountFactory.AccountType type = AccountFactory.AccountType.valueOf(accountType);
        Account newAccount = AccountFactory.createAccount(type,
                currency, iban, 0, 0, "alias", interestRate, owner, businessAmount);

        // add the new account to the list of accounts
        accounts.add(newAccount);
        hasAccount = true;
    }

    /**
     * Gets the user instance by matching the provided email.
     *
     * @param newEmail the email to match
     * @return the user instance if the email matches, null otherwise
     */
    public User getUserByEmail(final String newEmail) {
        if (this.email.equals(newEmail)) {
            return this;
        }
        return null;
    }

    /**
     * Gets the account by its IBAN.
     *
     * @param iban the IBAN to search for
     * @return the account with the matching IBAN, or null if not found
     */
    public Account getAccountByIBAN(final String iban) {
        for (Account account : accounts) {
            if (account.getIBAN().equals(iban)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Gets the account by its alias.
     *
     * @param alias the alias to search for
     * @return the account with the matching alias, or null if not found
     */
    public Account getAccountByAlias(final String alias) {
        for (Account account : accounts) {
            if (account.getAlias() != null && account.getAlias().equals(alias)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Gets the account by the card number associated with it.
     *
     * @param cardNumber the card number to search for
     * @return the account with the matching card number, or null if not found
     */
    public Account getAccountByCardNumber(final String cardNumber) {
        for (Account account : accounts) {
            if (account.getCardByNumber(cardNumber) != null) {
                return account;
            }
        }
        return null;
    }

    /**
     * Gets the transaction report for the user.
     *
     * @return the transaction report
     */
    public TransactionReport getTransactionReport() {
        return transactionReport;
    }

    /**
     * Adds a transaction to the user's transaction report.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactionReport.addTransaction(transaction);

        // Sort the transactions by timestamp after adding the new transaction
        transactionReport.getTransactions().sort(Comparator.comparing(Transaction::getTimestamp));
    }


    public double getAge() {
        // calculate the age of the user
        // the birth date is in the format yyyy-mm-dd
        String[] birthDateParts = birthDate.split("-");
        int birthYear = Integer.parseInt(birthDateParts[0]);
        int birthMonth = Integer.parseInt(birthDateParts[1]);
        int birthDay = Integer.parseInt(birthDateParts[2]);

        // get the current date
        java.util.Date currentDate = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentDate);
        String[] currentDateParts = formattedDate.split("-");
        int currentYear = Integer.parseInt(currentDateParts[0]);
        int currentMonth = Integer.parseInt(currentDateParts[1]);
        int currentDay = Integer.parseInt(currentDateParts[2]);

        // calculate the age
        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }
        return age;

    }

    public String getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(String servicePlan) {
        this.servicePlan = servicePlan;
    }

    public void upgradeServicePlan(String newServicePlan, double rate, Account account, int timestamp) {
        if (newServicePlan.equals("silver")) {
            // the user has to pay 100 RON to upgrade from student/standard to silver
            double amountToPay = rate * 100;
            if (account.getBalance() < amountToPay) {
                Transaction transaction = new UpgradePlanError(timestamp, "Insufficient funds");
                addTransaction(transaction);
                return;
            }
            account.setBalance(account.getBalance() - amountToPay);
            setServicePlan(newServicePlan);
        }
        else {
            if (getServicePlan().equals("silver")) {
                // the user has to pay 250 RON to upgrade from silver to gold
                double amountToPay = rate * 250;
                if (account.getBalance() < amountToPay) {
                    Transaction transaction = new UpgradePlanError(timestamp, "Insufficient funds");
                    addTransaction(transaction);
                    return;
                }
                account.setBalance(account.getBalance() - amountToPay);
                setServicePlan(newServicePlan);
            }
            else if(getServicePlan().equals("student") || getServicePlan().equals("standard")) {
                // the user has to pay 350 RON to upgrade from student/standard to gold
                double amountToPay = rate * 350;
                if (account.getBalance() < amountToPay) {
                    Transaction transaction = new UpgradePlanError(timestamp, "Insufficient funds");
                    addTransaction(transaction);
                    return;
                }
                account.setBalance(account.getBalance() - amountToPay);
                setServicePlan(newServicePlan);
            }
        }
        Transaction transaction = new UpgradePlanTransaction(timestamp, "Upgrade plan", newServicePlan, account.getIBAN());
        addTransaction(transaction);
        account.addTransaction(transaction);
    }

    public double addCommission(double amount, ExchangeRates exchangeRates, String currency) {

        if (getServicePlan().equals("student")) {
            return amount;
        }
        else if (getServicePlan().equals("standard")) {
            return amount + 0.002 * amount;
        }
        else if (getServicePlan().equals("silver")) {
            double rate = exchangeRates.convertExchangeRate(currency, "RON");
            double amountInRON = amount * rate;
            if (amountInRON < 500) {
                return amount;
            }
            else {
                return amount + 0.001 * amount;
            }
        }
        else if (getServicePlan().equals("gold")) {
            return amount;
        }
        return amount;
    }

    public void setAcceptCustomSplitPayment(int acceptCustomSplitPayment) {
        this.acceptCustomSplitPayment = acceptCustomSplitPayment;
    }

    public int getAcceptCustomSplitPayment() {
        return acceptCustomSplitPayment;
    }

    public void acceptSplitPayment(String splitPaymentType) {
        if (splitPaymentType.equals("custom")) {
            setAcceptCustomSplitPayment(1);
        }
        else {
            setAcceptEqualSplitPayment(1);
        }
    }

    public void rejectSplitPayment(String splitPaymentType) {
        if (splitPaymentType.equals("custom")) {
            setAcceptCustomSplitPayment(2);
        }
        else {
            setAcceptEqualSplitPayment(2);
        }
    }

    public void setAcceptEqualSplitPayment(int acceptEqualSplitPayment) {
        this.acceptEqualSplitPayment = acceptEqualSplitPayment;
    }

    public int getAcceptEqualSplitPayment() {
        return acceptEqualSplitPayment;
    }

    public boolean hasAcceptedCustomPayment() {
        if (acceptCustomSplitPayment == 1) {
            return true;
        }
        return false;
    }

    public boolean hasAcceptedEqualPayment() {
        if (acceptEqualSplitPayment == 1) {
            return true;
        }
        return false;
    }
}
