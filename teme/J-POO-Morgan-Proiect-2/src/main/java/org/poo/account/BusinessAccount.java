package org.poo.account;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.card.Card;
import org.poo.card.CardFactory;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.report.BusinessTransactionReport;
import org.poo.transaction.Transaction;
import org.poo.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessAccount implements Account {
    private String iban;
    private String accountType = "business";
    private String currency;
    private double balance;
    private double minBalance = 500;
    private double maxSpendLimit = 500;
    private double maxDepositedLimit = 500;
    private String alias;

    // cards is a list of all the cards that the user has in a specific account
    private ArrayList<Card> cards = new ArrayList<>();

    // commerciantsList is a list of all the commerciants that the user has sent money to
    // it will help in the spending report
    // it will help with the cashback strategy, counting the number of transactions for each commerciant
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

    private BusinessTransactionReport businessTransactionReport = new BusinessTransactionReport();

    public BusinessAccount(final String currency, final String iban, final double balance,
                           final double minBalance, User owner, final double businessLimit) {
        this.iban = iban;
        this.currency = currency;
        this.balance = 0;
        this.minBalance = 500;
        this.accountType = "business";
        this.owner = owner;
        this.maxSpendLimit = businessLimit;
        this.maxDepositedLimit = businessLimit;
    }

    @Override
    public String getIBAN() {
        return iban;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public double getMinBalance() {
        return minBalance;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String getType() {
        return accountType;
    }

    @Override
    public void setIBAN(final String iban) {
        // Set the IBAN of the account
        this.iban = iban;
    }

    @Override
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    @Override
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    @Override
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    @Override
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }


    @Override
    public void createCard(String type, String cardNumber, String email) {
        // Create a new card
        Card newCard = CardFactory.createCard(type, cardNumber, email);

        // Add the card to the list of cards
        cards.add(newCard);
    }

    @Override
    public boolean hasCards() {
        return !cards.isEmpty();
    }

    @Override
    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public Card getCardByNumber(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }



    public double getMaxSpendLimit() {
        return maxSpendLimit;
    }

    public void setMaxSpendLimit(double maxSpendLimit) {
        this.maxSpendLimit = maxSpendLimit;
    }

    public double getMaxDepositedLimit() {
        return maxDepositedLimit;
    }

    public void setMaxDepositedLimit(double maxDepositedLimit) {
        this.maxDepositedLimit = maxDepositedLimit;
    }

    public Map<User, Double> getManagerSpentAmounts() {
        return managerSpentAmounts;
    }

    public Map<User, Double> getEmployeeSpentAmount() {
        return employeeSpentAmount;
    }

    public void setManagerSpentAmounts(Map<User, Double> managerSpentAmounts) {
        if (managerSpentAmounts == null) {
            throw new IllegalArgumentException("Manager spent amounts map cannot be null");
        }
        this.managerSpentAmounts = managerSpentAmounts;
    }

    public void addManagerSpentAmount(User manager, double amount) {
        if (managerSpentAmounts.containsKey(manager)) {
            managerSpentAmounts.put(manager, managerSpentAmounts.get(manager) + amount);
        } else {
            managerSpentAmounts.put(manager, amount);
        }
    }

    public void addEmployeeSpentAmount(User employee, double amount) {
        if (employeeSpentAmount.containsKey(employee)) {
            employeeSpentAmount.put(employee, employeeSpentAmount.get(employee) + amount);
        } else {
            employeeSpentAmount.put(employee, amount);
        }
    }

    public void setEmployeeSpentAmount(Map<User, Double> employeeSpentAmount) {
        if (employeeSpentAmount == null) {
            throw new IllegalArgumentException("Employee spent amounts map cannot be null");
        }
        this.employeeSpentAmount = employeeSpentAmount;
    }

    public Map<User, Double> getManagerDepositedAmounts() {
        return managerDepositedAmounts;
    }

    public void setManagerDepositedAmounts(Map<User, Double> managerDepositedAmounts) {
        if (managerDepositedAmounts == null) {
            throw new IllegalArgumentException("Manager deposited amounts map cannot be null");
        }
        this.managerDepositedAmounts = managerDepositedAmounts;
    }

    public void addManagerDepositedAmount(User manager, double amount) {
        if (managerDepositedAmounts.containsKey(manager)) {
            managerDepositedAmounts.put(manager, managerDepositedAmounts.get(manager) + amount);
        } else {
            managerDepositedAmounts.put(manager, amount);
        }
    }

    public Map<User, Double> getEmployeeDepositedAmounts() {
        return employeeDepositedAmounts;
    }

    public void setEmployeeDepositedAmounts(Map<User, Double> employeeDepositedAmounts) {
        if (employeeDepositedAmounts == null) {
            throw new IllegalArgumentException("Employee deposited amounts map cannot be null");
        }
        this.employeeDepositedAmounts = employeeDepositedAmounts;
    }

    public void addEmployeeDepositedAmount(User employee, double amount) {
        if (employeeDepositedAmounts.containsKey(employee)) {
            employeeDepositedAmounts.put(employee, employeeDepositedAmounts.get(employee) + amount);
        } else {
            employeeDepositedAmounts.put(employee, amount);
        }
    }

    public Commerciant getCommerciantByIBAN(final String IBAN) {
        for (Commerciant commerciant : commerciantsList) {
            if (commerciant.getIban().equals(IBAN)) {
                return commerciant;
            }
        }
        return null;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void addTotalSpent(Double totalSpent) {
        this.totalSpent += totalSpent;
    }

    public Double getTotalDeposited() {
        return totalDeposited;
    }

    public void setTotalDeposited(Double totalDeposited) {
        this.totalDeposited = totalDeposited;
    }



    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getManagers() {
        return managers;
    }

    public void addManager(User manager) {
        managers.add(manager);
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void addEmployee(User employee) {
        employees.add(employee);
    }

    public boolean isEmployee(User user) {
        return employees.contains(user);
    }

    public boolean isManager(User user) {
        return managers.contains(user);
    }

    /**
     * Getter for the list of commerciants, which the user has sent money to
     * in online transactions.
     *
     * @return the list of commerciants
     */
    public ArrayList<Commerciant> getCommerciantList() {
        return commerciantsList;
    }

    /**
     * Method to add a commerciant to the list of commerciants.
     *
     * @param commerciant
     */
    public void addCommerciant(final Commerciant commerciant) {
        for (Commerciant c : commerciantsList) {
            if (c.getCommerciant().equals(commerciant.getCommerciant())) {
                c.addAmountSpent(commerciant.getAmountSpent());
                return;
            }
        }

        int index = 0;
        for (Commerciant c : commerciantsList) {
            if (c.getCommerciant().compareTo(commerciant.getCommerciant()) > 0) {
                break;
            }
            index++;
        }
        commerciantsList.add(index, commerciant);
    }

    public Commerciant getCommerciantByCommerciantName(final String commerciantName) {
        for (Commerciant commerciant : commerciantsList) {
            if (commerciant.getCommerciant().equals(commerciantName)) {
                return commerciant;
            }
        }
        return null;
    }

    public double getAmountSpentOnSTCommerciants() {
        return amountSpentOnSTCommerciants;
    }

    public void setAmountSpentOnSTCommerciants(double amountSpentOnSTCommerciants) {
        this.amountSpentOnSTCommerciants = amountSpentOnSTCommerciants;
    }

    public void addAmountSpentOnSTCommerciants(double amountSpent) {
        this.amountSpentOnSTCommerciants += amountSpent;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void addDiscount(final Discount discount) {
        discounts.add(discount);
    }

    public Discount getDiscountByType(final String type) {
        for (Discount discount : discounts) {
            if (discount.getType().equals(type)) {
                return discount;
            }
        }
        return null;
    }

    public ObjectNode getBusinessTransactionReport(int startTimestamp, int endTimestamp, int timestamp) {
        return businessTransactionReport.generateReportBetweenTimestamps(startTimestamp, endTimestamp, timestamp, this);

    }
}
