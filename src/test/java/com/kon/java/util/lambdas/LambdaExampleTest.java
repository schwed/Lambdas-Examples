package com.kon.java.util.lambdas;


import com.kon.java.util.OptionalExampleTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class LambdaExampleTest {

    private static final Logger logger = Logger.getLogger(OptionalExampleTest.class.getName());

    LambdaExamples.Formula formula1;
    List<String> names;

    // Lambda 4
    static int outerStaticNum;

    int outerNum;

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

        formula1 = new LambdaExamples.Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        formula1.calculate(100);     // 100.0
        formula1.sqrt(-23);          // 0.0
        LambdaExamples.Formula.positive(-4);        // 0.0

        //        Formula formula2 = (a) -> sqrt( a * 100);

        /**
         * names List
         */
        names = Arrays.asList("peter", "anna", "mike", "xenia");
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");

    }

    @Test
    @DisplayName("Lambda 1")
    void testLambda1() {

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        Collections.sort(names, (a, b) -> b.compareTo(a));

        System.out.println(names);

        names.sort(Collections.reverseOrder());

        System.out.println(names);

        List<String> names2 = Arrays.asList("peter", null, "anna", "mike", "xenia");
        names2.sort(Comparator.nullsLast(String::compareTo));
        System.out.println(names2);

        List<String> names3 = null;

        Optional.ofNullable(names3).ifPresent(list -> list.sort(Comparator.naturalOrder()));

        System.out.println(names3);
    }

    @Test
    @DisplayName("Lambda2 test")
    void testLambda2() {
        LambdaExamples.Converter<String, Integer> integerConverter1 = (from) -> Integer.valueOf(from);
        Integer converted1 = integerConverter1.convert("123");
        System.out.println(converted1);   // result: 123


        // method reference

        LambdaExamples.Converter<String, Integer> integerConverter2 = Integer::valueOf;
        Integer converted2 = integerConverter2.convert("123");
        System.out.println(converted2);   // result: 123


        LambdaExamples.Something something = new LambdaExamples.Something();

        LambdaExamples.Converter<String, String> stringConverter = something::startsWith;
        String converted3 = stringConverter.convert("Java");
        System.out.println(converted3);    // result J

        // constructor reference

        LambdaExamples.PersonFactory<LambdaExamples.Person> personFactory = LambdaExamples.Person::new;
        LambdaExamples.Person person = personFactory.create("Peter", "Parker");
    }

    @Test
    @DisplayName("Lambda3 test")
    void testLambda3() throws Exception {
        // Predicates

        Predicate<String> predicate = (s) -> s.length() > 0;

        predicate.test("foo");              // true
        predicate.negate().test("foo");     // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();


        // Functions

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);

        backToString.apply("123");     // "123"


        // Suppliers

        Supplier<LambdaExamples.Person> personSupplier = LambdaExamples.Person::new;
        personSupplier.get();   // new Person


        // Consumers

        Consumer<LambdaExamples.Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new LambdaExamples.Person("Luke", "Skywalker"));



        // Comparators

        Comparator<LambdaExamples.Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        LambdaExamples.Person p1 = new LambdaExamples.Person("John", "Doe");
        LambdaExamples.Person p2 = new LambdaExamples.Person("Alice", "Wonderland");

        comparator.compare(p1, p2);             // > 0
        comparator.reversed().compare(p1, p2);  // < 0


        // Runnables

        Runnable runnable = () -> System.out.println(UUID.randomUUID());
        runnable.run();


        // Callables

        Callable<UUID> callable = UUID::randomUUID;
        callable.call();
    }

    @Test
    @DisplayName("Lambda4 test")
    void testLambda4() {
        int num = 1;

        LambdaExamples.Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num);

        String convert = stringConverter.convert(2);
        System.out.println(convert);    // 3

        LambdaExamples.Converter<Integer, String> stringConverter2 = (from) -> {
            outerNum = 13;
            return String.valueOf(from);
        };

        String[] array = new String[1];
        LambdaExamples.Converter<Integer, String> stringConverter3 = (from) -> {
            array[0] = "Hi there";
            return String.valueOf(from);
        };

        stringConverter3.convert(23);

        System.out.println(array[0]);
    }

    @Test
    @DisplayName("Lambda5 test - Pre-Defined Functional Interfaces")
    void testLambda5() {
        //BiConsumer Example
        BiConsumer<String,Integer> printKeyAndValue
                = (key,value) -> System.out.println(key+"-"+value);

        printKeyAndValue.accept("One",1);
        printKeyAndValue.accept("Two",2);

        System.out.println("##################");

        //Java Hash-Map foreach supports BiConsumer
        HashMap<String, Integer> dummyValues = new HashMap<>();
        dummyValues.put("One", 1);
        dummyValues.put("Two", 2);
        dummyValues.put("Three", 3);

        dummyValues.forEach((key,value) -> System.out.println(key+"-"+value));
    }

}
