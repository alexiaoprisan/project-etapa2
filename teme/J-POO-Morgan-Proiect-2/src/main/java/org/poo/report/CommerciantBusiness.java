package org.poo.report;

import org.poo.user.User;

import java.util.ArrayList;

public class CommerciantBusiness {
    String commerciant;
    ArrayList<User> employees = new ArrayList<>();
    ArrayList<User> managers = new ArrayList<>();
    double totalAmountSpent = 0;

    public CommerciantBusiness(String commerciant) {
        this.commerciant = commerciant;
    }

    public void addEmployee(User employee) {
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


    public void addManager(User manager) {
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


    public void addAmountSpent(double amountSpent) {
        totalAmountSpent += amountSpent;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public ArrayList<User> getManagers() {
        return managers;
    }

    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }


}
