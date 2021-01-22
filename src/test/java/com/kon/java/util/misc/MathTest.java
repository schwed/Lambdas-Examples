package com.kon.java.util.misc;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class MathTest {

    private static final Logger logger = Logger.getLogger(MathTest.class.getName());

    @BeforeAll
    static void beforeAll() {
        System.out.println("A static method in your class that is called before all of its tests run");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("A static method in your test class that is after all od its test run");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("A method is called before each individual test rubs");
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");
    }

    @Test
    @DisplayName("test Math Exact")
    void testMathExact() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE + 1);

        try {
            Math.addExact(Integer.MAX_VALUE, 1);
        }
        catch (ArithmeticException e) {
            System.err.println(e.getMessage());
        }

        try {
            Math.toIntExact(Long.MAX_VALUE);
        }
        catch (ArithmeticException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    @DisplayName("test Math unsigned int")
    void testMathUnsignedInt() {
        try {
            Integer.parseUnsignedInt("-123", 10);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        long maxUnsignedInt = (1l << 32) - 1;
        System.out.println(maxUnsignedInt);

        String string = String.valueOf(maxUnsignedInt);

        int unsignedInt = Integer.parseUnsignedInt(string, 10);
        System.out.println(unsignedInt);

        String string2 = Integer.toUnsignedString(unsignedInt, 10);
        System.out.println(string2);

        try {
            Integer.parseInt(string, 10);
        }
        catch (NumberFormatException e) {
            System.err.println("could not parse signed int of " + maxUnsignedInt);
        }
    }
}
