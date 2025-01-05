package org.poo.user;

import org.poo.account.Account;
import org.poo.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a registry that manages a collection of users.
 * This class is responsible for adding, retrieving and searching for users.
 */
public final class UserRegistry {

    private final List<User> users = new ArrayList<>();

    private static UserRegistry instance;

    private UserRegistry() {
    }

    /**
     * Singleton instance getter.
     *
     * @return the singleton instance of the UserRegistry.
     */
    public static UserRegistry getInstance() {
        if (instance == null) {
            instance = new UserRegistry();
        }
        return instance;
    }

    /**
     * Clears the list of users.
     */
    public void reset() {
        users.clear();
    }

    /**
     * Getter for the list of users.
     *
     * @return a list of users.
     */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Adds a new user to the registry.
     *
     * @param user the user to add.
     */
    public void addUser(final User user) {
        users.add(user);
    }

    /**
     * Finds a user by their email address in the registry.
     *
     * @param email the email of the user.
     * @return the user with the given email, or null if not found.
     */
    public User getUserByEmail(final String email) {
        for (final User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds an account by its IBAN, searching through all users.
     *
     * @param iban the IBAN of the account.
     * @return the account with the given IBAN, or null if not found.
     */
    public Account getAccountByIBAN(final String iban) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getIBAN().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Finds an account by its alias, which is a name given to the
     * account, instead of the IBAN.
     *
     * @param alias the alias of the account.
     * @return the account with the given alias, or null if not found.
     */
    public Account getAccountByAlias(final String alias) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getAlias() != null && account.getAlias().equals(alias)) {
                    return account;
                }
            }
        }
        return null;
    }

    public Account getAccountByCardNumber(final String cardNumber) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds a card by its card number, searching through all users.
     *
     * @param cardNumber the card number.
     * @return the card with the given number, or null if not found.
     */
    public Card getCardByNumber(final String cardNumber) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds a user by the card number they hold.
     *
     * @param cardNumber the card number.
     * @return the user who holds the card, or null if not found.
     */
    public User getUserByCardNumber(final String cardNumber) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds a user by their account IBAN, searching through all users.
     *
     * @param iban the IBAN of the account.
     * @return the user who owns the account, or null if not found.
     */
    public User getUserByIBAN(final String iban) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getIBAN().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Finds a user by their account alias, searching through all users.
     *
     * @param alias the alias of the account.
     * @return the user who owns the account, or null if not found.
     */
    public User getUserByAlias(final String alias) {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getAlias() != null && account.getAlias().equals(alias)) {
                    return user;
                }
            }
        }
        return null;
    }

}
