package com.kon.java.util.streams;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.kon.java.util.streams.StreamsExamples.*;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.counting;

@ExtendWith(MockitoExtension.class)
public class StreamsExampleTest {

    private static final Logger logger = Logger.getLogger(StreamsExampleTest.class.getName());
    List<String> stringCollection;
    List<String> strings;
    List<Person> persons;


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
        stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        persons = Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("David", 12));

        strings = Arrays.asList("a1", "a2", "b1", "c2", "c1");
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");
    }

    @Test
    @DisplayName("filtering")
    void filteringTest() {
        stringCollection
                .stream()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);

        // "aaa2", "aaa1"
        logger.info("filtering of that starts with a: " + stringCollection);
    }

    @Test
    @DisplayName("sorting 2")
    void sortingTest2() {
        stringCollection
                .stream()
                .sorted()
                .forEach(System.out::println);

        logger.info("sorting 2" + stringCollection);
    }


    @Test
    @DisplayName("sorting")
    void sortingTest() {
        stringCollection
                .stream()
                .map(String::toUpperCase)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);

        // "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1

        logger.info("sorting id descending order: " + stringCollection);
    }


    @Test
    @DisplayName("matching")
    void matchingTest() {
        boolean anyStartsWithA = stringCollection
                .stream()
                .anyMatch((s) -> s.startsWith("a"));

        System.out.println(anyStartsWithA);      // true

        boolean allStartsWithA = stringCollection
                .stream()
                .allMatch((s) -> s.startsWith("a"));

        System.out.println(allStartsWithA);      // false

        boolean noneStartsWithZ = stringCollection
                .stream()
                .noneMatch((s) -> s.startsWith("z"));

        System.out.println(noneStartsWithZ);      // true
    }

    @Test
    @DisplayName("counting")
    void countingTest() {
        long startsWithB = stringCollection
                .stream()
                .filter((s) -> s.startsWith("b"))
                .count();

        System.out.println(startsWithB);    // 3
    }

    @Test
    @DisplayName("reducing")
    void reducingTest() {
        Optional<String> reduced =
                stringCollection
                        .stream()
                        .sorted()
                        .reduce((s1, s2) -> s1 + "#" + s2);

        reduced.ifPresent(System.out::println);
        // "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"

        logger.info("reducing: " + reduced.toString());
    }

    /**
     * Streams 3
     */
    @Test
    @DisplayName("streams sorting sequential")
    void sortSequentialTest() {
        final int MAX = 1000000;

        // sort sequential
        List<String> values = new ArrayList<>(MAX);
        for (int i = 0; i < MAX; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // sequential

        long t0 = System.nanoTime();

        long count = values.stream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));

    }

    @Test
    @DisplayName("stream sort parallel")
    void parallelTest() {

        final int MAX = 1000000;

        List<String> values = new ArrayList<>(MAX);
        for (int i = 0; i < MAX; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // parallel

        long t0 = System.nanoTime();

        long count = values.parallelStream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis));
    }

    /**
     * Streams 4 - filter, range, range reuse
     */

    @Test
    @DisplayName("range and reduce")
    void testStreams4() {
        logger.info("will print odd numbers in a regular for loop");
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 1) {
                System.out.println(i);
            }
        }

        logger.info("will print odd numbers using IntStream, range, forEach...");
        IntStream.range(0, 10)
                .forEach(i -> {
                    if (i % 2 == 1) System.out.println(i);
                });

        logger.info("will print odd numbers using IntStream, range, filter, forEach...");
        IntStream.range(0, 10)
                .filter(i -> i % 2 == 1)
                .forEach(System.out::println);

        logger.info("will print sum numbers using Optional, IntStream, range, reduce");
        OptionalInt reduced1 =
                IntStream.range(0, 10)
                        .reduce((a, b) -> a + b);
        System.out.println(reduced1.getAsInt());

        logger.info("will print sum number using IntStream, range, reduce using identity number");
        int reduced2 =
                IntStream.range(0, 10)
                        .reduce(7, (a, b) -> a + b);
        System.out.println(reduced2);
    }

    /**
     * streams 5
     */
    @Test
    @DisplayName("Streams5 - test 1 ")
    void test1() {
        stringCollection
                .stream()
                .filter(s -> {
                    System.out.println("filter:  " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    @Test
    @DisplayName("Streams5 test 2")
    void test2() {
        stringCollection
                .stream()
                .map(s -> {
                    System.out.println("map:     " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter:  " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    @Test
    @DisplayName("Streams5 test 3")
    void test3() {
        stringCollection
                .stream()
                .filter(s -> {
                    System.out.println("filter:  " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map:     " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));

    }

    @Test
    @DisplayName("Streams5 test 4")
    void test4() {

        logger.info("sorted horizontal");
        stringCollection
                .stream()
                .sorted((s1, s2) -> {
                    System.out.printf("sort:    %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter:  " + s);
                    return s.toLowerCase().startsWith("a");
                })
                .map(s -> {
                    System.out.println("map:     " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    @Test
    @DisplayName("Streams5 test 5")
    void test5() {
        stringCollection
                .stream()
                .filter(s -> {
                    System.out.println("filter:  " + s);
                    return s.toLowerCase().startsWith("a");
                })
                .sorted((s1, s2) -> {
                    System.out.printf("sort:    %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .map(s -> {
                    System.out.println("map:     " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    @Test
    @DisplayName("Streams5 test 6")
    void test6() {

        logger.info("short-circuit");

        stringCollection
                .stream()
                .map(s -> {
                    System.out.println("map:      " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });
    }

    @Test
    @DisplayName("Streams5 test 7")
    void test7() {

        logger.info("will trow exception stream has already been operated upon or closed");

        Stream<String> stream = stringCollection
                .stream()
                .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);
        stream.noneMatch(s -> true); // here IllegalStateException
    }

    @Test
    @DisplayName("Streams5 test 8")
    void test8() {
        Supplier<Stream<String>> streamSupplier =
                () -> stringCollection
                        .stream()
                        .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);
        streamSupplier.get().noneMatch(s -> true);
    }

    /**
     * Streams 7
     */
    @Test
    @DisplayName("Sreams 7 test1")
    void testSteams7Test1() {
        List<Foo> foos = new ArrayList<>();

        IntStream
                .range(1, 4)
                .forEach(num -> foos.add(new Foo("Foo" + num)));

        foos.forEach(f ->
                IntStream
                        .range(1, 4)
                        .forEach(num -> f.bars.add(new Bar("Bar" + num + " <- " + f.name))));

        foos.stream()
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    @Test
    @DisplayName("Streams 7 test 2")
    void testStreams7Test2() {
        IntStream.range(1, 4)
                .mapToObj(num -> new Foo("Foo" + num))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(num -> new Bar("Bar" + num + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    /**
     * Streams 8
     */
    @Test
    @DisplayName("Streams8 test")
    void testStreams8() {
        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);

        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);

        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);

        Arrays.stream(new int[] {1, 2, 3})
                .map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);

        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);

    }

    /**
     * Streams 9
     */
    @Test
    @DisplayName("Stereams 9 test")
    void testStream9() {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
           .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

        // C1
        // C2
    }

    /**
     * Streams 10
     */
    @Test
    @DisplayName("Streams 10 test 1 ")
    void testStream10Test1() {
        List<Person> filtered =
        persons
                .stream()
                .filter(p -> p.name.startsWith("P"))
                .collect(Collectors.toList());

        System.out.println(filtered);    // [Peter, Pamela]
    }

    @Test
    @DisplayName("Streams 10 test 2 ")
    void testStream10Test2() {
        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));

        personsByAge
                .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // age 18: [Max]
        // age 23:[Peter, Pamela]
        // age 12:[David]
    }

    @Test
    @DisplayName("Streams 10 test 3 ")
    void testStream10Test3() {
        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));

        System.out.println(averageAge);     // 19.0
    }

    @Test
    @DisplayName("Streams 10 test 4 ")
    void testStream10Test4() {
        IntSummaryStatistics ageSummary =
                persons
                        .stream()
                        .collect(Collectors.summarizingInt(p -> p.age));

        System.out.println(ageSummary);
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19,000000, max=23}
    }

    @Test
    @DisplayName("Streams 10 test 5 ")
    void testStream10Test5() {
        String names = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(names);
        // In Germany Max and Peter and Pamela are of legal age.
    }

    @Test
    @DisplayName("Streams 10 test 6 ")
    void testStream10Test6() {
        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));

        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}
    }

    @Test
    @DisplayName("Streams 10 test 7 ")
    void testStream10Test7() {
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    @Test
    @DisplayName("Streams 10 test 8 ")
    void testStream10Test8() {
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> {
                            System.out.println("supplier");
                            return new StringJoiner(" | ");
                        },
                        (j, p) -> {
                            System.out.format("accumulator: p=%s; j=%s\n", p, j);
                            j.add(p.name.toUpperCase());
                        },
                        (j1, j2) -> {
                            System.out.println("merge");
                            return j1.merge(j2);
                        },
                        j -> {
                            System.out.println("finisher");
                            return j.toString();
                        });

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    @Test
    @DisplayName("Streams 10 test 9 ")
    void testStream10Test9() {
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> {
                            System.out.println("supplier");
                            return new StringJoiner(" | ");
                        },
                        (j, p) -> {
                            System.out.format("accumulator: p=%s; j=%s\n", p, j);
                            j.add(p.name.toUpperCase());
                        },
                        (j1, j2) -> {
                            System.out.println("merge");
                            return j1.merge(j2);
                        },
                        j -> {
                            System.out.println("finisher");
                            return j.toString();
                        });

        String names = persons
                .parallelStream()
                .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    /**
     * Streams 11
     */
    @Test
    @DisplayName("Streams 11 test 1")
    void testStreams11Test1() {
        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela
    }

    @Test
    @DisplayName("Streams 11 test 2")
    void testStreams11Test2() {
        Person result =
                persons
                        .stream()
                        .reduce(new Person("", 0), (p1, p2) -> {
                            p1.age += p2.age;
                            p1.name += p2.name;
                            return p1;
                        });

        System.out.format("name=%s; age=%s", result.name, result.age);
    }

    @Test
    @DisplayName("Streams 11 test 3")
    void testStreams11Test3() {
        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);
    }

    @Test
    @DisplayName("Streams 11 test 4")
    void testStreams11Test4() {
        Integer ageSum = persons
                .stream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        System.out.println(ageSum);
    }

    @Test
    @DisplayName("Streams 11 test 5")
    void testStreams11Test5() {
        Integer ageSum = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        System.out.println(ageSum);
    }

    @Test
    @DisplayName("Streams 11 test 6")
    void testStreams11Test6() {
        Integer ageSum = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s; thread=%s\n",
                                    sum, p, Thread.currentThread().getName());
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s; thread=%s\n",
                                    sum1, sum2, Thread.currentThread().getName());
                            return sum1 + sum2;
                        });

        System.out.println(ageSum);
    }

    /**
     * Streams 12
     */

    @Test
    @DisplayName("Streams 12 test 1")
    void testStreams12Test1() {
        // -Djava.util.concurrent.ForkJoinPool.common.parallelism=5

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());
    }

    @Test
    @DisplayName("Streams 12 test 2")
    void testStreams12Test2() {
        strings
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter:  %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map:     %s [%s]\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));

    }

    @Test
    @DisplayName("Streams 12 test 3")
    void testStreams12Test3() {
        strings
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter:  %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map:     %s [%s]\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort:    %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
    }

    @Test
    @DisplayName("Streams 12 test 4")
    void testStreams12Test4() {
        List<String> values = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // sequential

        long t0 = System.nanoTime();

        long count = values
                .parallelStream()
                .sorted((s1, s2) -> {
                    System.out.format("sort:    %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis));
    }

    @Test
    @DisplayName("Streams 13 test")
    void testStreams13() {
        SecureRandom secureRandom = new SecureRandom(new byte[]{1, 3, 3, 7});
        int[] randoms = IntStream.generate(secureRandom::nextInt)
                .filter(n -> n > 0)
                .limit(10)
                .toArray();
        System.out.println(Arrays.toString(randoms));


        int[] nums = IntStream.iterate(1, n -> n * 2)
                .limit(11)
                .toArray();
        System.out.println(Arrays.toString(nums));
    }

    /**
     * Collectors - groupingBy
     */
    @Test
    @DisplayName("List - groupBy and display the total count of it")
    void testListGroupBy() {
        List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");

        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), counting()
                        )
                );

        System.out.println(result); // papaya=1, orange=1, banana=2, apple=3

    }

    @Test
    @DisplayName("Add sorting to ist - groupBy and display the total count of it")
    void testListGroupBySorting() {
        List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");

        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), counting()
                        )
                );

        Map<String, Long> finalMap = new LinkedHashMap<>();

        //Sort a map and add to finalMap
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap); // apple=3, banana=2, papaya=1, orange=1
    }

    /**
     * Example of group by a List of user defined Objects
     */
    @Test
    @DisplayName("‘group by’ a list of user defined Objects")
    void testGroupByListOfObjects() {
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orange", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );

        Map<String, Long> counting = items.stream().collect(
                Collectors.groupingBy(Item::getName, counting()));

        System.out.println(counting); // papaya=1, banana=2, apple=3, orang=1, watermelon=1

        Map<String, Integer> sum = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));

        System.out.println(sum); //Group by + Sum qty: papaya=20, banana=30, apple=40, orange=10, watermelon=10

    }

    /**
     * Group by Price – Collectors.groupingBy and Collectors.mapping example.
     */
    @Test
    @DisplayName("List - Group by Price – Collectors.groupingBy and Collectors.mapping example.")
    void testListGroupByPrice() {
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orange", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );

        //group by price
        Map<BigDecimal, List<Item>> groupByPriceMap =
                items.stream().collect(Collectors.groupingBy(Item::getPrice));

        System.out.println(groupByPriceMap);
        /*
        output of groupBy price:

        {
            19.99=[
            Item{name='banana', qty=20, price=19.99},
            Item{name='banana', qty=10, price=19.99}
		    ],
            29.99=[
            Item{name='orange', qty=10, price=29.99},
            Item{name='watermelon', qty=10, price=29.99}
		    ],
            9.99=[
            Item{name='apple', qty=10, price=9.99},
            Item{name='papaya', qty=20, price=9.99},
            Item{name='apple', qty=10, price=9.99},
            Item{name='apple', qty=20, price=9.99}
		    ]
        }
         */

        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> result =
                items.stream().collect(
                        Collectors.groupingBy(Item::getPrice,
                                Collectors.mapping(Item::getName, Collectors.toSet())
                        )
                );

        System.out.println(result);

        /*
        //group by + mapping to Set (no duplicates!)
        {
            19.99=[banana],
            29.99=[orange, watermelon],
            9.99=[papaya, apple]
        }
         */

    }


    @Test
    @DisplayName("List of Strings duplicates")
    void testListOfStringsDuplicates() {
        List<String> items =
                Arrays.asList("blackPants", "redShirt", "whiteSweater", "redShirt", "blackTShirt",
                        "blueShirt", "whiteSweater", "greenJacket", "blueJacket", "blackPants");

        System.out.println(Math.pow(10, 4));
        if (items.size() > Math.pow(10, 4)) {
            return;
        }

        /**
         * find duplicates using Set and  - this is the best performance as it loops only once
         */

        Set<String> tempSet = new HashSet<>();
        List<String> resultList =
                items.stream().sorted()
                .filter(n -> !tempSet.add(n))
                .collect(Collectors.toList());

        resultList.forEach(System.out::println);

        /**
         *  find duplicates using groupingBy
         */


        List<String> result =
                items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(m -> m.getValue() > 1)
                .map(Map.Entry::getKey).distinct().sorted().collect(Collectors.toList());

        result.forEach(System.out::println);

        /**
         *  find duplicates by frequency
         */

        result.clear();

        result =
                items.stream()
                .filter(i -> Collections.frequency(items, i ) > 1)
                        .sorted().distinct()
                .collect(Collectors.toList());

        result.forEach(System.out::println);

        /*
        output in all three cases above:
        blackPants
        redShirt
        whiteSweater
         */



    }

    /**
     * from h0tk3y/Mainjava github
     */
    @Test
    @DisplayName("h0tk3y/ Mainjava  - mapExample")
    void testMapExample() {
        String s = "1 2 3 4 5 6 7 8 9 10";

        System.out.println("1. Mapping with a one-argument function:");
        List<Integer> ints = Arrays.stream(s.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
        System.out.println(ints);

        System.out.println("2. Mapping with a simple lambda expression:");
        List<Double> pow2 = ints.stream().map(x -> Math.pow(2.0, x)).collect(Collectors.toList());
        System.out.println(pow2);
        List<String> doubled = ints.stream().map(i -> i + "_" + i).collect(Collectors.toList());
        System.out.println(doubled);

        System.out.println("3. Mapping with complex function:");
        // Though this one can be simplified to the form in (2). Try it.
        List<Integer> pows = pow2.stream().map(d -> {
            String str = Double.toString(d);
            return str.length();
        }).collect(Collectors.toList());
        System.out.println(pows);

        // Everywhere else where you have to pass a function or a predicate, you can use one of the three forms:
        // 1. Function reference, e.g. `Integer::parseInt`
        // 2. One-expression lambda, e.g `x -> Integer.parseInt(x)`
        // 3. Lambda with body, e.g. `x -> { return Integer.parseInt(x); }`
    }

    @Test
    @DisplayName("h0tk3y/ Mainjava  - filterExample")
    void testFilterExample() {
        List<String> words = Arrays.asList("tst", "one", "two", "three", "four", "five", "rotator", "deified");

        System.out.println("1. Simple filtering");
        List<String> longWords = words.stream().filter(s -> s.length() > 3).collect(Collectors.toList());
        System.out.println(longWords);

        System.out.println("2. A little more complex filtering");
        List<String> palindromes = words.stream()
                .filter(s -> s.equals(new StringBuilder(s).reverse().toString()))
                .collect(Collectors.toList());
        System.out.println(palindromes);

    }

    @Test
    @DisplayName("h0tk3y/ Mainjava  - aggregatingOperationsExamples")
    void testAggregateOperationsExample() {
        List<String> words = Arrays.stream("Once upon a midnight dreary while I pondered weak and weary".split("\\s+"))
                .collect(Collectors.toList());

        System.out.println("1. Check if all items satisfy a criterion:");
        boolean noWordsLongerThan9 = words.stream().allMatch(w -> w.length() <= 9);
        boolean theSame = words.stream().noneMatch(w -> w.length() > 9);
        System.out.println(noWordsLongerThan9);

        System.out.println("2. Check if any item satisfies a criterion:");
        boolean hasShortWords = words.stream().anyMatch(w -> w.length() < 3);
        System.out.println(hasShortWords);

        System.out.println("3. Average over the items");
        //Note the last call. If stream contains no items, it will return 0.0;
        double average = words.stream().mapToInt(String::length).average().orElse(0.0);
        System.out.println(average);

        System.out.println("4. Count items that satisfy a criterion:");
        long wordsWithE = words.stream().filter(w -> w.contains("e")).count();
        System.out.println(wordsWithE);

        System.out.println("5. Max and sum");
        int maxWordLength = words.stream().mapToInt(String::length).max().orElse(0); //min is the same
        int sumWordLength = words.stream().mapToInt(String::length).sum();
        System.out.println("max: " + maxWordLength + ", sum: " + sumWordLength);
    }

    @Test
    @DisplayName("h0tk3y/ Mainjava  - moreFunnyExample")
    void testMoreFunnyExample() {
        List<String> words = Arrays.stream("These examples cover only a small part of Java 8 Streams.".split("\\s+"))
                .collect(Collectors.toList());

        System.out.println("1. Join to string:");
        String noDelimiter = words.stream().collect(Collectors.joining());
        System.out.println(noDelimiter);
        String withDelimiter = words.stream().map(it -> Integer.toString(it.length())).collect(Collectors.joining(" + "));
        System.out.println(withDelimiter);

        System.out.println("2. Concat two streams:");
        List<String> concat = Stream.concat(words.stream(),
                words.stream().map(s -> new StringBuilder(s).reverse().toString()))
                .collect(Collectors.toList());
        System.out.println(concat);

        System.out.println("3. Create a Map from stream");
        //toMap takes two arguments: the first is the function to get keys (we pass identity there -- it is a function
        //that returns exactly its argument, like `x -> x`, and the second one to get values.
        Map<String, Integer> map = words.stream().collect(Collectors.toMap(Function.identity(), String::length));
        System.out.println(map);

        System.out.println("4. Distinct items of a stream:");
        //Here's also a way to create a stream directly from values
        Set<Integer> unique = Stream.of(5, 1, 2, 3, 4, 5, 4, 3, 2, 1).collect(Collectors.toSet());
        System.out.println(unique);

        System.out.println("5. Sorted stream");
        List<String> sorted = Stream.of("a", "z", "b", "c").sorted().collect(Collectors.toList());
        System.out.println(sorted);

        System.out.println("6. Limit and skip");
        List<Integer> part = Stream.of(1, 2, 3, 4, 5, 7, 8, 9).skip(2).limit(4).collect(Collectors.toList());
        System.out.println(part);

        System.out.println("7. Production of ints in the stream");
        // This example is a bit complex, but it can be useful to learn the concept of `reducing`.
        // `reduce` is an operation with an accumulator: we provide an initial value for the accumulator and a function
        // with two arguments. As one of the arguments the current accumulator value will always be passed.
        // The function is applied to every item in the stream and its result is the next value of the accumulator.
        //
        // Now let's try to implement ints production which, unfortunately, is not there along with `sum` and `average`.
        int production = Stream.of(1, 2, 3, 5, 7, 11).reduce(1, (acc, i) -> acc * i);
        System.out.println(production);

        System.out.println("8. Int range");
        // Yeah, `mapToObj(i -> i)` looks weird, but in Java IntStream and Stream are not the same, and this is the
        // way to convert one to another.
        List<Integer> range = IntStream.range(1, 10).mapToObj(i -> i).collect(Collectors.toList());
        System.out.println(range);
        // Uses the production
        int factorial = range.stream().reduce(1, (acc, i) -> acc * i);
        System.out.println(factorial);

        System.out.println("9. Flat map");
        // Flat map is another concept which is not that simple, but might be useful.
        // Flat map is similar to map: it iterates over every item in the stream and calls the function which you
        // pass as the argument. But your function should return not a single value but a stream. And flat map then
        // concatenates all the streams returned.
        List<Character> chars = words.stream()
                .flatMap(w -> w.chars().mapToObj(c -> (char) c))
                .collect(Collectors.toList());
        System.out.println(chars);

        System.out.println("10. Group by");
        // The result is a map where for each value of the function passed to groupingBy there is a list of the stream
        // items which had this value.
        Map<Integer, List<String>> wordsByLength = words.stream().collect(Collectors.groupingBy(String::length));
        System.out.println(wordsByLength);
    }


}
