package org.poo.report;

import org.poo.user.User;

import java.util.ArrayList;

/**
 * The CommerciantBusiness class manages a commercian's employees, managers,
 * and the total amount spent by the business.
 */
public final class CommerciantBusiness {

    // Private fields with accessor methods.
    private final String commerciant;
    private final ArrayList<User> employees = new ArrayList<>();
    private final ArrayList<User> managers = new ArrayList<>();
    private double totalAmountSpent = 0;

    /**
     * Constructor to initialize the commercian name.
     *
     * @param commerciant The name of the commerciant, should not change.
     */
    public CommerciantBusiness(final String commerciant) {
        this.commerciant = commerciant;
    }

    /**
     * Adds an employee to the list, ensuring the employee is placed alphabetically by last name.
     *
     * @param employee The employee to add.
     */
    public void addEmployee(final User employee) {
        // Add the employee alphabetically by last name
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getLastName().compareTo(employee.getLastName()) > 0) {
                employees.add(i, employee);
                return;
            }
        }
        // If no earlier position was found, add at the end
        employees.add(employee);
    }

    /**
     * Adds a manager to the list, ensuring the manager is placed alphabetically by last name.
     *
     * @param manager The manager to add.
     */
    public void addManager(final User manager) {
        // Add the manager alphabetically by last name
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getLastName().compareTo(manager.getLastName()) > 0) {
                managers.add(i, manager);
                return;
            }
        }
        // If no earlier position was found, add at the end
        managers.add(manager);
    }

    /**
     * Adds the total amount spent by the business.
     *
     * @param amountSpent The amount spent to add to the total.
     */
    public void addAmountSpent(final double amountSpent) {
        totalAmountSpent += amountSpent;
    }

    /**
     * Retrieves the name of the commerciant.
     *
     * @return The name of the commerciant.
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * Retrieves the list of employees.
     *
     * @return The list of employees.
     */
    public ArrayList<User> getEmployees() {
        return employees;
    }

    /**
     * Retrieves the list of managers.
     *
     * @return The list of managers.
     */
    public ArrayList<User> getManagers() {
        return managers;
    }

    /**
     * Retrieves the total amount spent by the business.
     *
     * @return The total amount spent.
     */
    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }
}
