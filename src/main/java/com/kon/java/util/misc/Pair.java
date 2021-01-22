package com.kon.java.util.misc;

public class Pair {
    public static final Pair SEED = new Pair(1, 1);

    public final int index;
    public final double value;

    public Pair(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public Pair next() {
        return new Pair(index + 1, Math.pow(index + 1, 2));
    }
}
