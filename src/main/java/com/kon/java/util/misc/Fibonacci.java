package com.kon.java.util.misc;

public class Fibonacci {
    public static final Fibonacci SEED = new Fibonacci(1, 1);

    public final int previous;
    public final int value;


    public Fibonacci(int previous, int value) {
        this.previous = previous;
        this.value = value;
    }

    public Fibonacci next() {
        return new Fibonacci(value, value + previous);
    }

}
