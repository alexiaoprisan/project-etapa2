package org.poo.exchangeRates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a collection of exchange rates and provides utilities
 * to add, find, and convert exchange rates between currencies.
 */
public final class ExchangeRates {

    private static ExchangeRates instance;

    private final ArrayList<ExchangeInputFormat> exchangeRates = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private ExchangeRates() {
    }

    /**
     * Singleton instance getter.
     *
     * @return the singleton instance of the ExchangeRates
     */
    public static ExchangeRates getInstance() {
        if (instance == null) {
            instance = new ExchangeRates();
        }
        return instance;
    }

    /**
     * Clears the list of exchange rates.
     */
    public void reset() {
        exchangeRates.clear();
    }

    /**
     * Adds a new exchange rate.
     *
     * @param exchangeInput the exchange rate to add
     */
    public void addExchangeRate(final ExchangeInputFormat exchangeInput) {
        exchangeRates.add(exchangeInput);
    }

    /**
     * Returns the list of exchange rates.
     *
     * @return the list of exchange rates
     */
    public ArrayList<ExchangeInputFormat> getExchangeRates() {
        return new ArrayList<>(exchangeRates);
    }

    /**
     * Adds a new exchange rate to the list.
     *
     * @param exchangeInput the exchange rate to add
     */
    public void addExchangeRates(final ExchangeInputFormat exchangeInput) {
        exchangeRates.add(exchangeInput);
    }

    /**
     * Finds and adds reciprocal exchange rates to the collection.
     * It will help when the exchange rate from A to B is known, but the rate from B to A is not and
     * wth conversion between two currencies.
     */
    public void findNewExchangeRates() {
        ArrayList<ExchangeInputFormat> newRates = new ArrayList<>();

        for (ExchangeInputFormat exchange : exchangeRates) {
            if (exchange.getRate() == 0) {
                continue;
            }

            // add the reciprocal exchange rate
            double amount = 1.0 / exchange.getRate();
            ExchangeInputFormat newExchange = new ExchangeInputFormat(
                    exchange.getTo(), exchange.getFrom(), amount, exchange.getTimestamp());
            newRates.add(newExchange);
        }

        exchangeRates.addAll(newRates);
    }

    /**
     * Converts an exchange rate between two currencies using a graph.
     * The currencies are represented as nodes in the graph.
     * The map contains as keys the source currencies and as values the
     * list of exchange rates.
     * The graph will use breadth-first search to find the exchange rate
     * between two currencies.
     *
     * @param currencyFrom the source currency
     * @param currencyTo   the target currency
     * @return the converted exchange rate, or 0 if not available
     */
    public double convertExchangeRate(final String currencyFrom,
                                      final String currencyTo) {
        // build a graph
        Map<String, List<ExchangeInputFormat>> graph = new HashMap<>();

        // populate the graph with the exchange rates
        for (ExchangeInputFormat exchange : exchangeRates) {
            if (!graph.containsKey(exchange.getFrom())) {
                graph.put(exchange.getFrom(), new ArrayList<>());
            }
            graph.get(exchange.getFrom()).add(exchange);
        }

        // build a queue, which will help with bfs (breadth-first search)
        Queue<ExchangeInputFormat> queue = new LinkedList<>();

        // keep track of visited currencies
        Set<String> visited = new HashSet<>();

        // start with the currencyFrom
        queue.add(new ExchangeInputFormat(currencyFrom, currencyFrom, 1.0, 0));

        // bfs
        while (!queue.isEmpty()) {
            // remove the first element from the queue
            ExchangeInputFormat current = queue.poll();

            // get the currency and rate
            String currentCurrency = current.getFrom();
            double currentRate = current.getRate();

            // mark the currency as visited
            visited.add(currentCurrency);

            // get neighbors of the current currency
            List<ExchangeInputFormat> neighbors = graph.getOrDefault(currentCurrency,
                    new ArrayList<>());
            for (ExchangeInputFormat neighbor : neighbors) {
                String nextCurrency = neighbor.getTo();
                double nextRate = currentRate * neighbor.getRate();

                // check if the target currency is reached
                if (nextCurrency.equals(currencyTo)) {
                    // return the exchange rate
                    return nextRate;
                }

                // if the currency is not visited, add it to the queue
                if (!visited.contains(nextCurrency)) {
                    queue.add(new ExchangeInputFormat(nextCurrency, nextCurrency, nextRate, 0));
                }
            }
        }

        // return 0 if the target currency was not reached
        return 0;
    }
}
