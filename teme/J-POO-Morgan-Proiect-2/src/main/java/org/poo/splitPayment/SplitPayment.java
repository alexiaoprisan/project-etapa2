package org.poo.splitPayment;

import org.poo.user.User;
import org.poo.user.UserRegistry;

import java.util.*;

public class SplitPayment {
    private List<String> accountsIBAN = new ArrayList<>();
    private List<Double> amountForEachAccount = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private String currency;
    private String splitPaymentType;
    private Double totalAmount;
    private int timestamp;

    public SplitPayment() {

    }

    public void addUserPayment(String accountIban, double amount, User user) {
        accountsIBAN.add(accountIban);
        amountForEachAccount.add(amount);
        users.add(user);
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setSplitPaymentType(String splitPaymentType) {
        this.splitPaymentType = splitPaymentType;
    }

    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getAccountsIBAN() {
        return accountsIBAN;
    }

    public List<Double> getAmountForEachAccount() {
        return amountForEachAccount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public boolean checkIfUserIsInPaymentByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfUserIsInPayment(User user) {
        return users.contains(user);
    }

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
