package org.poo.discounts;

public class Discount {
    private double value;
    private String type; // food or clothes or tech
    private boolean isUsed = false;

    public Discount(double value, String type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void use() {
        this.isUsed = true;
    }


}
