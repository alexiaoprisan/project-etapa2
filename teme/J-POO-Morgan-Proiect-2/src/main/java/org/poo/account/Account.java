package org.poo.account;

import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
import org.poo.discounts.Discount;
import org.poo.transaction.Transaction;

import java.util.ArrayList;

/**
 * Interface for the Account class
 */
public interface Account {

    /**
     * Getter for the currency of a specific account
     *
     * @return the currency of the account
     */
    String getCurrency();

    /**
     * Getter for the account type
     *
     * @return the account type
     */
    String getAccountType();

    /**
     * Getter for the IBAN of the account
     *
     * @return the IBAN of the account
     */
    String getIBAN();

    /**
     * Getter for the balance of the account (the amount of money in the account)
     *
     * @return the balance of the account
     */
    double getBalance();

    /**
     * Getter for the minimum balance of the account (the minimum amount of money
     * that can be in the account)
     *
     * @return the minimum balance of the account
     */
    double getMinBalance();

    /**
     * Getter for the alias (a name for the account that can be used instead of the IBAN)
     *
     * @return the alias of the account
     */
    String getAlias();

    /**
     * Getter for the type of the account (classic or savings)
     *
     * @return the type of the account
     */
    String getType();

    /**
     * Getter for the cards of the account
     *
     * @return an array list of the cards of the account
     */
    ArrayList<Card> getCards();

    /**
     * Method to get the card by its number
     *
     * @param cardNumber
     * @return the card with the given number
     */
    Card getCardByNumber(String cardNumber);

    /**
     * Setter for the currency of the account
     *
     * @param currency
     */
    void setCurrency(String currency);

    /**
     * Setter for the account type
     *
     * @param accountType
     */
    void setAccountType(String accountType);

    /**
     * Setter for the IBAN of the account
     *
     * @param iban
     */
    void setIBAN(String iban);

    /**
     * Setter for the balance of the account
     *
     * @param balance
     */
    void setBalance(double balance);

    /**
     * Setter for the minimum balance of the account (the minimum amount of
     * money that can be in the account)
     *
     * @param minBalance
     */
    void setMinBalance(double minBalance);

    /**
     * Setter for the alias (a name for the account that can be used instead of the IBAN)
     *
     * @param alias
     */
    void setAlias(String alias);


    /**
     * Method to create a card for the account
     * An account can have multiple cards
     *
     * @param type       (regular or oneTimeCard)
     * @param cardNumber (generated when it is created)
     */
    void createCard(String type, String cardNumber);

    /**
     * Method to check if the account has cards
     *
     * @return true if the account has cards, false otherwise
     */
    boolean hasCards();

    /**
     * Method to add a card in the list of cards of the account
     *
     * @param card
     */
    void addCard(Card card);


    /**
     * Method to add a transaction to the account, which will be useful for the reports
     *
     * @param transaction
     */
    void addTransaction(Transaction transaction);

    /**
     * Getter for the list of commerciants, which the user has sent money to
     * in online transactions.
     *
     * @return the list of commerciants
     */
    public ArrayList<Commerciant> getCommerciantList();

    /**
     * Method to add a commerciant to the list of commerciants.
     *
     * @param commerciant
     */
    public void addCommerciant(final Commerciant commerciant);

    public Commerciant getCommerciantByCommerciantName(final String commerciantName);

    double getAmountSpentOnSTCommerciants();

    void setAmountSpentOnSTCommerciants(double amountSpentOnSTCommerciants);

    public void addAmountSpentOnSTCommerciants(double amountSpent);

    public ArrayList<Discount> getDiscounts();

    public void addDiscount(final Discount discount);

    public Discount getDiscountByType(final String type);

}
