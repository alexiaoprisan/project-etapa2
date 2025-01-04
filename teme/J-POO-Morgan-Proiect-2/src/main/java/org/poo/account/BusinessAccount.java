package org.poo.account;

import org.poo.card.Card;
import org.poo.card.CardFactory;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

public class BusinessAccount implements Account {
    private String iban;
    private String accountType;
    private String currency;
    private double balance;
    private double minBalance;
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

    public BusinessAccount(final String currency, final String iban, final double balance, final double minBalance) {
        this.iban = iban;
        this.currency = currency;
        this.balance = 0;
        this.minBalance = minBalance;
        this.accountType = "business";
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
    public void createCard(String type, String cardNumber) {
        // Create a new card
        Card newCard = CardFactory.createCard(type, cardNumber);

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
