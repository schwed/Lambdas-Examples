package com.kon.java.util.lambdas;

public class LambdaExamples {

    public static class Person {
        String firstName;
        String lastName;

        public Person() {}

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public interface Formula {
        double calculate(int a);

        default double sqrt(int a) {
            return Math.sqrt(positive(a));
        }

        static int positive(int a) {
            return a > 0 ? a : 0;
        }
    }

    /**
     * Lambda 2
     * @param <F>
     * @param <T>
     */
    @FunctionalInterface
    public static interface Converter<F, T> {
        T convert(F from);
    }

    static class Something {
        String startsWith(String s) {
            return String.valueOf(s.charAt(0));
        }
    }

    interface PersonFactory<P extends Person> {
        P create(String firstName, String lastName);
    }

    /**
     * Lambda 3
     */

    @FunctionalInterface
    interface Fun {
        void foo();
    }

}
