package org.poo.commerciants;

/**
 * The Commerciant class represents a commerciant, which
 * appears in a payOnline transaction.
 * It will be used for the spendings report.
 */
public final class Commerciant {
    private String commerciant;
    private int timestamp;

    int id;
    String iban;
    String type;
    String cashbackStrategy;

    // cate tranzactii a facut un cont la un comerciant (adica de cate ori a platit la kfc de exemplu)
    int nrOfTransactions;

    private double amountSpent = 0;

    /**
     * Constructor for the Commerciant class.
     *
     * @param name        The commerciant of the commerciant
     * @param amountSpent The amount spent by the account
     * @param timestamp   The timestamp of the transaction
     */
    public Commerciant(final String name, final double amountSpent, final int timestamp) {
        this.commerciant = name;
        this.amountSpent = amountSpent;
        this.timestamp = timestamp;
    }

    public Commerciant(final String name, final int id, final String iban, final String type, final String cashbackStrategy) {
        this.commerciant = name;
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Constructor for the Commerciant class.
     */
    public Commerciant() {
    }

    /**
     * Getter for the commerciant of the commerciant.
     *
     * @return The commerciant of the commerciant.
     */
    public String getCommerciant() {
        return this.commerciant;
    }

    /**
     * Getter for the timestamp of the commerciant.
     *
     * @return The timestamp of the commerciant.
     */
    public int getTimestamp() {
        return this.timestamp;
    }

    /**
     * Getter for the amount spent by the user.
     *
     * @return The amount spent by the user.
     */
    public double getAmountSpent() {
        return this.amountSpent;
    }

    /**
     * Setter for the commerciant of the commerciant.
     *
     * @param commerciant The commerciant of the commerciant.
     */
    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

    /**
     * Setter for the amount spent by the user.
     *
     * @param amountSpent The amount spent by the user.
     */
    public void setAmountSpent(final double amountSpent) {
        this.amountSpent = amountSpent;
    }

    /**
     * Setter for the timestamp of the commerciant.
     *
     * @param timestamp The timestamp of the commerciant.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Method to add an amount spent by the user.
     *
     * @param amount The amount to be added to the total amount spent
     *               when buying from the commerciant.
     */
    public void addAmountSpent(final double amount) {
        this.amountSpent += amount;
    }

    // int id;
    //    String iban;
    //    String type;
    //    String cashbackStrategy;
    //
    //    int nrOfTransactions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCashbackStrategy() {
        return cashbackStrategy;
    }

    public void setCashbackStrategy(String cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public int getNrOfTransactions() {
        return nrOfTransactions;
    }

    public void setNrOfTransactions(int nrOfTransactions) {
        this.nrOfTransactions = nrOfTransactions;
    }

    public void incrementNrOfTransactions() {
        this.nrOfTransactions++;
    }



}
