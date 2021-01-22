package com.kon.java.util;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalExample {
    public static class Framework {

        String name;
        int communityUsers;
    }

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        }
        catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    static class Outer {
        Nested nested = new Nested();
        public Nested getNested() {
            return nested;
        }
    }

    static class Nested {
        Inner inner = new Inner();
        public Inner getInner() {
            return inner;
        }
    }

    static class Inner {
        String foo = "boo";
        public String getFoo() {
            return foo;
        }
    }



}
