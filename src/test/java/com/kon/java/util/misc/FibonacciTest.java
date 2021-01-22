package com.kon.java.util.misc;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @ExtendWith(MockitoExtension.class).
 * The @ExtendWith annotation is used to load a JUnit 5 extension.
 * JUnit defines an extension API, which allows a third-party vendor like Mockito
 * to hook into the lifecycle of running test classes and add additional functionality.
 */
@ExtendWith(MockitoExtension.class)
public class FibonacciTest {

    /**
     * Hamcrest is based on the concept of a matcher,
     * which can be a very natural way of asserting whether or not the result of a test is in a desired state.
     *
     * Hamcrest defines the following common matchers:
     * Objects: equalTo, hasToString, instanceOf, isCompatibleType, notNullValue, nullValue, sameInstance
     * Text: equalToIgnoringCase, equalToIgnoringWhiteSpace, containsString, endsWith, startsWith
     * Numbers: closeTo, greaterThan, greaterThanOrEqualTo, lessThan, lessThanOrEqualTo
     * Logical: allOf, anyOf, not
     * Collections: array (compare an array to an array of matchers), hasEntry, hasKey, hasValue, hasItem, hasItems, hasItemInArray
     */
    private static final Logger logger = Logger.getLogger(FilesTest.class.getName());

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
    @DisplayName("Fibonacci and Streams")
    void testFibonacci() {

        Stream.iterate(Fibonacci.SEED, Fibonacci::next);
    }

    @Test
    @DisplayName("Pair test")
    void testPair() {
        /*
        Stream.iterate(new Pair(1, 1),
                pair -> new Pair(pair.index + 1, Math.pow(pair.index + 1, 2)));
        */

        Stream.iterate(Pair.SEED, Pair::next);
    }

    @Test
    @DisplayName("Factorial test")
    void testFactorial() {

        Stream.iterate(Factorial.SEED, Factorial::next);
    }

}
