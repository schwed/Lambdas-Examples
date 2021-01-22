package com.kon.java.util.misc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class StringTest {

    private static final Logger logger = Logger.getLogger(StringTest.class.getName());

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
        System.out.println("A method is called before each individual test run");
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test run");
    }

    @Test
    @DisplayName("String test Pattern Split")
    void testPatternSplit() {
        String string = Pattern.compile(":")
                .splitAsStream("foobar:foo:bar")
                .filter(s -> s.contains("bar"))
                .sorted()
                .collect(Collectors.joining(":"));
        System.out.println(string);
    }

    @Test
    @DisplayName("String test Pattern Predicate")
    void testPatternPredicate() {
        long count = Stream.of("bob@gmail.com", "alice@hotmail.com")
                .filter(Pattern.compile(".*@gmail\\.com").asPredicate())
                .count();
        System.out.println(count);
    }

    @Test
    @DisplayName("String test Chars")
    void testPatternChars() {
        String string = "foobar:foo:bar"
                .chars()
                .distinct()
                .mapToObj(c -> String.valueOf((char) c))
                .sorted()
                .collect(Collectors.joining());
        System.out.println(string);
    }

    @Test
    @DisplayName("String test Join")
    void testJoin() {
        String string = String.join(":", "foobar", "foo", "bar");
        System.out.println(string);
    }




}
