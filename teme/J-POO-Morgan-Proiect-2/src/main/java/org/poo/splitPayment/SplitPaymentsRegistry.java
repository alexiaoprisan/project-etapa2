package org.poo.splitPayment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a registry that manages a collection of split payments.
 * It ensures a single instance is maintained throughout the application lifecycle.
 */
public final class SplitPaymentsRegistry {

    private final List<SplitPayment> splitPayments = new ArrayList<>();
    private static SplitPaymentsRegistry instance;

    // Private constructor to prevent instantiation
    private SplitPaymentsRegistry() {
    }

    /**
     * Singleton instance getter.
     *
     * @return the singleton instance of the SplitPaymentsRegistry.
     */
    public static SplitPaymentsRegistry getInstance() {
        if (instance == null) {
            instance = new SplitPaymentsRegistry();
        }
        return instance;
    }

    /**
     * Adds a new split payment to the registry.
     *
     * @param splitPayment the split payment to add.
     */
    public void addSplitPayment(final SplitPayment splitPayment) {
        splitPayments.add(splitPayment);
    }

    /**
     * Retrieves an unmodifiable view of all split payments.
     *
     * @return a list of split payments.
     */
    public List<SplitPayment> getSplitPayments() {
        return Collections.unmodifiableList(splitPayments);
    }

    /**
     * Resets the registry, clearing all split payments.
     */
    public void reset() {
        splitPayments.clear();
    }

    /**
     * Retrieves a split payment based on the user's email and split payment type.
     *
     * @param email the email of the user.
     * @param splitPaymentType the type of split payment.
     * @return the split payment if found, otherwise null.
     */
    public SplitPayment getSplitPaymentByUserEmail(final String email,
                                                   final String splitPaymentType) {
        for (final SplitPayment splitPayment : splitPayments) {
            if (splitPayment.checkIfUserIsInPaymentByEmail(email)
                    && splitPayment.getSplitPaymentType().equals(splitPaymentType)) {
                return splitPayment;
            }
        }
        return null;
    }

    /**
     * Removes the specified split payment from the registry.
     *
     * @param splitPayment the split payment to remove.
     */
    public void removeSplitPayment(final SplitPayment splitPayment) {
        splitPayments.remove(splitPayment);
    }
}
