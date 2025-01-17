package org.poo.commerciants;

import java.util.ArrayList;
import java.util.List;

/**
 * The CommerciantRegistry class manages the collection of commerciants.
 * It provides methods to add, reset, and retrieve commerciants from the registry.
 */
public final class CommerciantRegistry {

    private List<Commerciant> commerciants = new ArrayList<>();

    private static CommerciantRegistry instance;

    private CommerciantRegistry() {
    }

    /**
     * Returns the single instance of CommerciantRegistry.
     *
     * @return The CommerciantRegistry instance.
     */
    public static CommerciantRegistry getInstance() {
        if (instance == null) {
            instance = new CommerciantRegistry();
        }
        return instance;
    }

    /**
     * Resets the list of commerciants by clearing it.
     */
    public void reset() {
        commerciants.clear();
    }

    /**
     * Gets the list of all commerciants.
     *
     * @return The list of all commerciants.
     */
    public List<Commerciant> getCommerciant() {
        return commerciants;
    }

    /**
     * Adds a new commerciant to the registry.
     *
     * @param commerciant The commerciant to be added.
     */
    public void addCommerciant(final Commerciant commerciant) {
        commerciants.add(commerciant);
    }

    /**
     * Retrieves a commerciant by their name.
     *
     * @param commerciantName The name of the commerciant to find.
     * @return The commerciant with the specified name, or null if not found.
     */
    public Commerciant getCommerciantByName(final String commerciantName) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getCommerciant().equals(commerciantName)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * Retrieves a commerciant by their IBAN.
     *
     * @param iban The IBAN of the commerciant to find.
     * @return The commerciant with the specified IBAN, or null if not found.
     */
    public Commerciant getCommerciantByIBAN(final String iban) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getIban().equals(iban)) {
                return commerciant;
            }
        }
        return null;
    }
}
