package org.poo.account;

import org.poo.card.Card;
import org.poo.card.CardFactory;
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






}
