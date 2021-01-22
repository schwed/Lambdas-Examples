package com.kon.java.util.nashorn;

public class Person {
    private String name;
    private String surName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLengthOfName() {
        return name.length();
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Person(String name, String surName) {
        this.name = name;
        this.surName = surName;
    }

    public Person() {
    }
}
