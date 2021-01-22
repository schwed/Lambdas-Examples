package com.kon.java.util.misc;

public class Factorial {
    public static final Factorial SEED = new Factorial(1, 1);

    public final int index;
    public final int value;

    public Factorial(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public Factorial next() {
        return new Factorial(index + 1, value * index);
    }
}
