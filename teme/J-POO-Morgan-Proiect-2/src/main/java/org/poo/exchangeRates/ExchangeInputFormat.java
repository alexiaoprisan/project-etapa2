package org.poo.exchangeRates;

/**
 * This class represents the input format for exchange rate data,
 * including the source and target currencies, exchange rate and a timestamp.
 */
public final class ExchangeInputFormat {

    private String from;
    private String to;
    private double rate;
    private int timestamp;

    /**
     * Constructor for ExchangeInputFormat.
     *
     * @param from      the source currency
     * @param to        the target currency
     * @param rate      the exchange rate from the source to the target currency
     * @param timestamp the timestamp associated with this exchange rate
     */
    public ExchangeInputFormat(final String from,
                               final String to,
                               final double rate,
                               final int timestamp) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    /**
     * Default constructor for ExchangeInputFormat.
     */
    public ExchangeInputFormat() {
    }

    /**
     * Getter for the source currency.
     *
     * @return the source currency
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setter for the source currency.
     *
     * @param from the source currency
     */
    public void setFrom(final String from) {
        this.from = from;
    }

    /**
     * Getter for the target currency.
     *
     * @return the target currency
     */
    public String getTo() {
        return to;
    }

    /**
     * Setter for the target currency.
     *
     * @param to the target currency
     */
    public void setTo(final String to) {
        this.to = to;
    }

    /**
     * Getter for the exchange rate.
     *
     * @return the exchange rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Setter for the exchange rate.
     *
     * @param rate the exchange rate
     */
    public void setRate(final double rate) {
        this.rate = rate;
    }

    /**
     * Getter for the timestamp.
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}
