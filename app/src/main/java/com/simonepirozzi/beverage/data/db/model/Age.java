package com.simonepirozzi.beverage.data.db.model;

public class Age {
    private int days;
    private int months;
    private int years;

    public Age(int days, int months, int years) {
        this.days = days;
        this.months = months;
        this.years = years;
    }

    public Age(){}

    public int getDays() {
        return this.days;
    }

    public int getMonths() {
        return this.months;
    }

    public int getYears() {
        return this.years;
    }

}
