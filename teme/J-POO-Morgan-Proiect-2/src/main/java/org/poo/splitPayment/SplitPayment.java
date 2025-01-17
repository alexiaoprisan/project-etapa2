package org.poo.splitPayment;

import org.poo.user.User;
import java.util.List;
import java.util.ArrayList;

/**
 * The SplitPayment class handles the distribution of payments between multiple users.
 */
public final class SplitPayment {
    private List<String> accountsIBAN = new ArrayList<>();
    private List<Double> amountForEachAccount = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private String currency;
    private String splitPaymentType;
    private Double totalAmount;
    private int timestamp;

    public SplitPayment() {
    }

    /**
     * Adds a user payment to the list.
     *
     * @param accountIban the IBAN of the account
     * @param amount the amount for the account
     * @param user the user associated with the payment
     */
    public void addUserPayment(final String accountIban, final double amount, final User user) {
        accountsIBAN.add(accountIban);
        amountForEachAccount.add(amount);
        users.add(user);
    }

    /**
     * Sets the timestamp of the payment.
     *
     * @param timestamp the timestamp of the payment
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the timestamp of the payment.
     *
     * @return the timestamp of the payment
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the currency of the payment.
     *
     * @param currency the currency of the payment
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets the currency of the payment.
     *
     * @return the currency of the payment
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the split payment type.
     *
     * @param splitPaymentType the split payment type
     */
    public void setSplitPaymentType(final String splitPaymentType) {
        this.splitPaymentType = splitPaymentType;
    }

    /**
     * Gets the split payment type.
     *
     * @return the split payment type
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    /**
     * Gets the users associated with the payment.
     *
     * @return the users associated with the payment
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Gets the IBAN of the accounts.
     *
     * @return the IBAN of the accounts
     */
    public List<String> getAccountsIBAN() {
        return accountsIBAN;
    }

    /**
     * Gets the amount for each account.
     *
     * @return the amount for each account
     */
    public List<Double> getAmountForEachAccount() {
        return amountForEachAccount;
    }

    /**
     * Sets the total amount of the payment.
     *
     * @param totalAmount the total amount of the payment
     */
    public void setTotalAmount(final Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the total amount of the payment.
     *
     * @return the total amount of the payment
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Checks if a user is part of this payment by their email.
     *
     * @param email the email of the user
     * @return true if the user is in the payment, false otherwise
     */
    public boolean checkIfUserIsInPaymentByEmail(final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a user is part of this payment.
     *
     * @param user the user
     * @return true if the user is in the payment, false otherwise
     */
    public boolean checkIfUserIsInPayment(final User user) {
        return users.contains(user);
    }

    /**
     * Checks if all users have accepted the payment.
     *
     * @return true if all users have accepted the payment, false otherwise
     */
    public boolean checkIfAllUsersAcceptedPayment() {
        for (User user : users) {
            if (splitPaymentType.equals("equal") && !user.hasAcceptedEqualPayment()) {
                return false;
            }

            if (splitPaymentType.equals("custom") && !user.hasAcceptedCustomPayment()) {
                return false;
            }
        }
        return true;
    }
}
