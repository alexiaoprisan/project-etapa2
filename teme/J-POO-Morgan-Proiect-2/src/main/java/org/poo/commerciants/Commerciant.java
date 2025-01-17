package org.poo.commerciants;

/**
 * The Commerciant class represents a commerciant, which
 * appears in a payOnline transaction.
 * It will be used for the spendings report.
 */
public final class Commerciant {
    private String commerciant;
    private int timestamp;
    private int id;
    private String iban;
    private String type;
    private String cashbackStrategy;

    // how many transactions the user has made to this commerciant
    private int nrOfTransactions;
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

    /**
     * Constructor for the Commerciant class.
     *
     * @param name             The commerciant of the commerciant
     * @param id               The ID of the commerciant
     * @param iban             The IBAN of the commerciant
     * @param type             The type of the commerciant
     * @param cashbackStrategy The cashback strategy of the commerciant
     */
    public Commerciant(final String name,
                       final int id,
                       final String iban,
                       final String type,
                       final String cashbackStrategy) {
        this.commerciant = name;
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Default constructor for the Commerciant class.
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
     * Setter for the commerciant of the commerciant.
     *
     * @param commerciant The commerciant of the commerciant.
     */
    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
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
     * Setter for the timestamp of the commerciant.
     *
     * @param timestamp The timestamp of the commerciant.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
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
     * Setter for the amount spent by the user.
     *
     * @param amountSpent The amount spent by the user.
     */
    public void setAmountSpent(final double amountSpent) {
        this.amountSpent = amountSpent;
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

    /**
     * Getter for the ID of the commerciant.
     *
     * @return The ID of the commerciant.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the ID of the commerciant.
     *
     * @param id The ID of the commerciant.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Getter for the IBAN of the commerciant.
     *
     * @return The IBAN of the commerciant.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Setter for the IBAN of the commerciant.
     *
     * @param iban The IBAN of the commerciant.
     */
    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * Getter for the type of the commerciant.
     *
     * @return The type of the commerciant.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the type of the commerciant.
     *
     * @param type The type of the commerciant.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Getter for the cashback strategy of the commerciant.
     *
     * @return The cashback strategy of the commerciant.
     */
    public String getCashbackStrategy() {
        return cashbackStrategy;
    }

    /**
     * Setter for the cashback strategy of the commerciant.
     *
     * @param cashbackStrategy The cashback strategy of the commerciant.
     */
    public void setCashbackStrategy(final String cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Getter for the number of transactions made to the commerciant.
     *
     * @return The number of transactions made to the commerciant.
     */
    public int getNrOfTransactions() {
        return nrOfTransactions;
    }

    /**
     * Setter for the number of transactions made to the commerciant.
     *
     * @param nrOfTransactions The number of transactions made to the commerciant.
     */
    public void setNrOfTransactions(final int nrOfTransactions) {
        this.nrOfTransactions = nrOfTransactions;
    }

    /**
     * Increment the number of transactions made to the commerciant by one.
     */
    public void incrementNrOfTransactions() {
        this.nrOfTransactions++;
    }
}
