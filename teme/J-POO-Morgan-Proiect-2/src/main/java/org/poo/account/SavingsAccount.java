package org.poo.account;

import org.poo.card.Card;
import org.poo.card.CardFactory;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.report.SavingsReport;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Class which represents a savings account with specific fields and methods.
 * A savings account is different from a classic account because it has an
 * interest rate field.
 * Implements the Account interface.
 * <p>
 * This class is not designed for extension.
 */
public final class SavingsAccount implements Account {
    private String currency;
    private String accountType;
    private String iban;
    private double balance;
    private double minBalance;
    private String alias;
    private double interestRate;

    // report is a list of all the transactions made by the user in the account
    // only the transactions that represented interest earnings or changes in interest
    private SavingsReport report = new SavingsReport();

    // cards is a list of all the cards that the user has in a specific account
    private final ArrayList<Card> cards = new ArrayList<>();

    // Constructor
    public SavingsAccount(final String currency, final String iban, final double balance,
                          final double minBalance, final double interestRate) {
        this.currency = currency;
        this.accountType = "savings";
        this.iban = iban;
        this.balance = balance;
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    // commerciantsList is a list of all the commerciants that the user has sent money to
    // it will help in the spending report
    // it will help with the cashback strategy, counting the number of transactions for each commerciant
    private final ArrayList<Commerciant> commerciantsList = new ArrayList<>();

    // amountSpentOnSTCommerciants is the amount of money spent on the commerciants who
    // have a cashback strategy of type SpendingThreshold
    private double amountSpentOnSTCommerciants = 0;

    // discounts is a list of all the discounts that the user has
    private ArrayList<Discount> discounts = new ArrayList<>();

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
    public String getAccountType() {
        return accountType;
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
    public String getAlias() {
        return alias;
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
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIBAN(final String newIban) {
        this.iban = newIban;
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
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return accountType;
    }

    /**
     * Getter for the interest rate of the account
     *
     * @return the interest rate of the account
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter for the interest rate of the account
     *
     * @param interestRate the interest rate to set
     */
    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
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
    public void createCard(final String type, final String cardNumber) {
        // Create and add a new card
        Card newCard = CardFactory.createCard(type, cardNumber);
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
    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
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
        report.addTransaction(transaction);
    }

    /**
     * Getter for the report of the account
     *
     * @return the report of the account
     */
    public SavingsReport getReport() {
        return report;
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
}
