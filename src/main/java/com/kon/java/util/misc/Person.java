package com.kon.java.util.misc;

public class Person {
    private String companyName;
    private int employeeNumber;

    public Person() {
    }

    public Person(String companyName, int employeeNumber) {
        this.companyName = companyName;
        this.employeeNumber = employeeNumber;
    }

    public String getCompanyName() {

        return companyName;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public int getEmployeeNumber() {

        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {

        this.employeeNumber = employeeNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "companyName='" + companyName + '\'' +
                ", employeeNumber=" + employeeNumber +
                '}';
    }
}
