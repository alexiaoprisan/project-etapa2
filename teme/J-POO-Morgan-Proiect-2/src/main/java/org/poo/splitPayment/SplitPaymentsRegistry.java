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
    public void addSplitPayment(SplitPayment splitPayment) {
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

    public SplitPayment getSplitPaymentByUserEmail(String email, String splitPaymentType) {
        for (SplitPayment splitPayment : splitPayments) {
            if (splitPayment.checkIfUserIsInPaymentByEmail(email) && splitPayment.getSplitPaymentType().equals(splitPaymentType)) {
                return splitPayment;
            }

        }
        return null;
    }

    public void removeSplitPayment(SplitPayment splitPayment) {
        splitPayments.remove(splitPayment);
    }


}
