package org.poo.commerciants;

import java.util.ArrayList;
import java.util.List;

public class CommerciantRegistry {

    private List<Commerciant> commerciants = new ArrayList<>();

    private static CommerciantRegistry instance;

    private CommerciantRegistry() {
    }


    public static CommerciantRegistry getInstance() {
        if (instance == null) {
            instance = new CommerciantRegistry();
        }
        return instance;
    }

    public void reset() {
        commerciants.clear();
    }

    public List<Commerciant> getCommerciant() {
        return commerciants;
    }

    public void addCommerciant(final Commerciant commerciant) {
        commerciants.add(commerciant);
    }

    public Commerciant getCommerciantByName(final String commerciantName) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getCommerciant().equals(commerciantName)) {
                return commerciant;
            }
        }
        return null;
    }


}
