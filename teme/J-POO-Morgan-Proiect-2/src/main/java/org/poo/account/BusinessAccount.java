package org.poo.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.card.Card;
import org.poo.card.CardFactory;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.report.BusinessCommerciantReport;
import org.poo.report.BusinessTransactionReport;
import org.poo.transaction.Transaction;
import org.poo.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BusinessAccount implements Account {
    private static final int MINIMUM_BALANCE = 500;
    private String iban;
    private String accountType = "business";
    private String currency;
    private double balance;
    private double minBalance = MINIMUM_BALANCE;
    private double maxSpendLimit = MINIMUM_BALANCE;
    private double maxDepositedLimit = MINIMUM_BALANCE;
    private String alias;

    // cards is a list of all the cards that the user has in a specific account
    private ArrayList<Card> cards = new ArrayList<>();

    // commerciantsList is a list of all the commerciants that the user has sent money to
    // it will help in the spending report
    // it will help with the cashback strategy, counting the transactions for each commerciant
    private final ArrayList<Commerciant> commerciantsList = new ArrayList<>();

    // amountSpentOnSTCommerciants is the amount of money spent on the commerciants who
    // have a cashback strategy of type SpendingThreshold
    private double amountSpentOnSTCommerciants = 0;

    // discounts is a list of all the discounts that the user has
    private ArrayList<Discount> discounts = new ArrayList<>();

    private User owner;

    // managers and employees are lists of users that have access to the account
    private List<User> managers = new ArrayList<>();
    private List<User> employees = new ArrayList<>();

    // maps for the amount spent by the managers and employees, will help in transaction report
    private Map<User, Double> managerSpentAmounts = new HashMap<>();
    private Map<User, Double> employeeSpentAmount = new HashMap<>();

    // maps for the amount deposited by the managers and employees, will help in transaction report
    private Map<User, Double> managerDepositedAmounts = new HashMap<>();
    private Map<User, Double> employeeDepositedAmounts = new HashMap<>();

    // total spent and total deposited will help in the commerciant report
    private Double totalSpent = 0.0;
    private Double totalDeposited = 0.0;

    // the transactions report
    private BusinessTransactionReport businessTransactionReport = new BusinessTransactionReport();

    // the commercaints report
    private BusinessCommerciantReport businessCommerciantReport = new BusinessCommerciantReport();

    public BusinessAccount(final String currency, final String iban, final double balance,
                           final double minBalance, final User owner, final double businessLimit) {
        this.iban = iban;
        this.currency = currency;
        this.balance = 0;
        this.minBalance = MINIMUM_BALANCE;
        this.accountType = "business";
        this.owner = owner;
        this.maxSpendLimit = businessLimit;
        this.maxDepositedLimit = businessLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIBAN() {
        return iban;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrency() {
        return currency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getBalance() {
        return balance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMinBalance() {
        return minBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccountType() {
        return accountType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return accountType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIBAN(final String newIban) {
        // Set the IBAN of the account
        this.iban = newIban;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCard(final String type, final String cardNumber, final String email) {
        // Create a new card
        Card newCard = CardFactory.createCard(type, cardNumber, email);

        // Add the card to the list of cards
        cards.add(newCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCards() {
        return !cards.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Card getCardByNumber(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTransaction(final Transaction transaction) {

    }

    /**
     * Getter for the maximum spend limit of the account, which can be set only by the owner
     *
     * @return the maximum spending limit
     */
    public double getMaxSpendLimit() {
        return maxSpendLimit;
    }

    /**
     * Setter for the maximum spend limit of the account, which can be set only by the owner
     *
     * @param maxSpendLimit the maximum spending limit
     */
    public void setMaxSpendLimit(final double maxSpendLimit) {
        this.maxSpendLimit = maxSpendLimit;
    }

    /**
     * Getter for the maximum deposited limit of the account, which can be set only by the owner
     *
     * @return the maximum deposited limit
     */
    public double getMaxDepositedLimit() {
        return maxDepositedLimit;
    }

    /**
     * Setter for the maximum deposited limit of the account, which can be set only by the owner
     *
     * @param maxDepositedLimit the maximum deposited limit
     */
    public void setMaxDepositedLimit(final double maxDepositedLimit) {
        this.maxDepositedLimit = maxDepositedLimit;
    }

    /**
     * Getter for the map of the amounts spent by the managers
     *
     * @return the map of the amounts spent by the managers
     */
    public Map<User, Double> getManagerSpentAmounts() {
        return managerSpentAmounts;
    }

    /**
     * Getter for the map of the amounts spent by the employees
     *
     * @return the map of the amounts spent by the employees
     */
    public Map<User, Double> getEmployeeSpentAmount() {
        return employeeSpentAmount;
    }

    /**
     * Add a manager to the map of the amounts spent by the managers
     *
     * @param manager the manager
     * @param amount  the amount spent by the manager
     */
    public void addManagerSpentAmount(final User manager, final double amount) {
        if (managerSpentAmounts.containsKey(manager)) {
            managerSpentAmounts.put(manager, managerSpentAmounts.get(manager) + amount);
        } else {
            managerSpentAmounts.put(manager, amount);
        }
    }

    /**
     * Add an employee to the map of the amounts spent by the employees
     *
     * @param employee the employee
     * @param amount   the amount spent by the employee
     */
    public void addEmployeeSpentAmount(final User employee, final double amount) {
        if (employeeSpentAmount.containsKey(employee)) {
            employeeSpentAmount.put(employee, employeeSpentAmount.get(employee) + amount);
        } else {
            employeeSpentAmount.put(employee, amount);
        }
    }

    /**
     * Getter for the map of the amounts deposited by the managers
     *
     * @return the map of the amounts deposited by the managers
     */
    public Map<User, Double> getManagerDepositedAmounts() {
        return managerDepositedAmounts;
    }


    /**
     * Add a manager to the map of the amounts deposited by the managers
     *
     * @param manager the manager
     * @param amount  the amount deposited by the manager
     */
    public void addManagerDepositedAmount(final User manager, final double amount) {
        if (managerDepositedAmounts.containsKey(manager)) {
            managerDepositedAmounts.put(manager, managerDepositedAmounts.get(manager) + amount);
        } else {
            managerDepositedAmounts.put(manager, amount);
        }
    }

    /**
     * Getter for the map of the amounts deposited by the employees
     *
     * @return the map of the amounts deposited by the employees
     */
    public Map<User, Double> getEmployeeDepositedAmounts() {
        return employeeDepositedAmounts;
    }

    /**
     * Add an employee to the map of the amounts deposited by the employees
     *
     * @param employee the employee
     * @param amount   the amount deposited by the employee
     */
    public void addEmployeeDepositedAmount(final User employee, final double amount) {
        if (employeeDepositedAmounts.containsKey(employee)) {
            employeeDepositedAmounts.put(employee, employeeDepositedAmounts.get(employee) + amount);
        } else {
            employeeDepositedAmounts.put(employee, amount);
        }
    }

    /**
     * Method to get a commerciant by its iban.
     *
     * @param accountIban the iban of the commerciant
     * @return the commerciant with the given iban
     */
    public Commerciant getCommerciantByIBAN(final String accountIban) {
        for (Commerciant commerciant : commerciantsList) {
            if (commerciant.getIban().equals(accountIban)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * Method to get the total amount spent in the account
     *
     * @return the total amount spent
     */
    public Double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Method to set the total amount spent in the account
     *
     * @param totalSpent the total amount spent
     */
    public void setTotalSpent(final Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     * Method to add the total amount spent in the account
     *
     * @param total the total amount spent
     */
    public void addTotalSpent(final Double total) {
        this.totalSpent += total;
    }

    /**
     * Method to get the total amount deposited in the account
     *
     * @return the total amount deposited
     */
    public Double getTotalDeposited() {
        return totalDeposited;
    }

    /**
     * Method to set the total amount deposited in the account
     *
     * @param totalDeposited the total amount deposited
     */
    public void setTotalDeposited(final Double totalDeposited) {
        this.totalDeposited = totalDeposited;
    }

    /**
     * Method to add the total amount deposited in the account
     *
     * @return the total amount deposited
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Method to set the owner of the account
     *
     * @param owner the owner of the account
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * Method to get the list of managers
     *
     * @return the list of managers
     */
    public List<User> getManagers() {
        return managers;
    }

    /**
     * Method to add a manager to the list of managers
     *
     * @param manager the manager to be added
     */
    public void addManager(final User manager) {
        // Add the manager only if not present in employees
        if (!employees.contains(manager) && owner != manager) {
            managers.add(manager);
        }
    }

    /**
     * Method to get the list of employees
     *
     * @return the list of employees
     */
    public List<User> getEmployees() {
        return employees;
    }

    /**
     * Method to add an employee to the list of employees
     *
     * @param employee the employee to be added
     */
    public void addEmployee(final User employee) {
        if (!managers.contains(employee) && owner != employee) {
            employees.add(employee);
        }
    }

    /**
     * Method to check if a user is an employee of the account
     *
     * @param user the user to be checked
     * @return true if the user is an employee, false otherwise
     */
    public boolean isEmployee(final User user) {
        return employees.contains(user);
    }

    /**
     * Method to check if a user is a manager of the account
     *
     * @param user the user to be checked
     * @return true if the user is a manager, false otherwise
     */
    public boolean isManager(final User user) {
        return managers.contains(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Commerciant> getCommerciantList() {
        return commerciantsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCommerciant(final Commerciant commerciant) {
        for (Commerciant c : commerciantsList) {
            if (c.getCommerciant().equals(commerciant.getCommerciant())) {
                return;
            }
        }

        int index = 0;
        for (Commerciant c : this.commerciantsList) {
            if (c.getCommerciant().compareTo(commerciant.getCommerciant()) > 0) {
                break;
            }
            index++;
        }
        Commerciant newCommerciant = new Commerciant(commerciant.getCommerciant(),
                commerciant.getId(),
                commerciant.getIban(),
                commerciant.getType(),
                commerciant.getCashbackStrategy());
        commerciantsList.add(index, newCommerciant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDiscount(final Discount discount) {
        discounts.remove(discount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Commerciant getCommerciantByCommerciantName(final String commerciantName) {
        for (Commerciant commerciant : commerciantsList) {
            if (commerciant.getCommerciant().equals(commerciantName)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAmountSpentOnSTCommerciants() {
        return amountSpentOnSTCommerciants;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAmountSpentOnSTCommerciants(final double amountSpentOnSTCommerciants) {
        this.amountSpentOnSTCommerciants = amountSpentOnSTCommerciants;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAmountSpentOnSTCommerciants(final double amountSpent) {
        this.amountSpentOnSTCommerciants += amountSpent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDiscount(final Discount discount) {
        discounts.add(discount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Discount getDiscountByType(final String type) {
        for (Discount discount : discounts) {
            if (discount.getType().equals(type)) {
                return discount;
            }
        }
        return null;
    }

    /**
     * Get the business transaction report
     *
     * @param startTimestamp the start timestamp
     * @param endTimestamp   the end timestamp
     * @param timestamp      the current timestamp
     * @return the business transaction report
     */
    public ObjectNode getBusinessTransactionReport(final int startTimestamp,
                                                   final int endTimestamp,
                                                   final int timestamp) {
        return businessTransactionReport.generateReportBetweenTimestamps(startTimestamp,
                endTimestamp, timestamp, this);

    }

    /**
     * Get the business commerciant report
     *
     * @return the business commerciant report
     */
    public BusinessCommerciantReport getBusinessCommerciantReport() {
        return businessCommerciantReport;
    }

    /**
     * Set the business commerciant report
     *
     * @param businessCommerciantReport
     */
    public void setBusinessCommerciantReport(final BusinessCommerciantReport
                                                     businessCommerciantReport) {
        this.businessCommerciantReport = businessCommerciantReport;
    }
}
