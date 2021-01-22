package com.kon.java.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static com.kon.java.util.OptionalExample.resolve;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OptionalExampleTest {

    private static final Logger logger = Logger.getLogger(OptionalExampleTest.class.getName());

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
    @DisplayName("optional begin streams tests")
    void optionalTestsBegins() {
        Optional<String> optional = Optional.of("bam");

        optional.isPresent();           // true
        optional.get();                 // "bam"
        optional.orElse("fallback");    // "bam"

        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b
        logger.info("optional: " + optional.get());
    }

    @Test
    @DisplayName("optional get")
    public void optional_get() {

        OptionalExample.Framework framework = new OptionalExample.Framework();
        framework.communityUsers = 200000;
        framework.name = "Java";

        Optional<OptionalExample.Framework> optionalFramework = Optional.of(framework);

        assertEquals("Java", optionalFramework.get().name);


        logger.info("communityUsers: " + optionalFramework.get().communityUsers);
    }

    @Test
    @DisplayName("optional ifPresent")
    public void optional_ifPresent() {

        OptionalExample.Framework framework = new OptionalExample.Framework();
        framework.communityUsers = 200000;
        framework.name = "Java";

        Optional<OptionalExample.Framework> optionalFramework = Optional.of(framework);

        optionalFramework.ifPresent(p -> System.out.println(p.name));

        // or

        optionalFramework.ifPresent(System.out::println);

        logger.info("ifPresent: " + optionalFramework.isPresent());
    }


    @Test
    @DisplayName("optional isPresent")
    public void optional_isPresent() {

        Optional<OptionalExample.Framework> framework = Optional.of(new OptionalExample.Framework());

        assertTrue(framework.isPresent());

        logger.info("isPresent: " + framework.isPresent());
    }

    @Test
    @DisplayName("optional orElse")
    public void optional_orElse() {

        OptionalExample.Framework framework = new OptionalExample.Framework();
        framework.communityUsers = 200000;
        framework.name = "Java";

        Optional<OptionalExample.Framework> nullOptional = Optional.ofNullable(null);

        OptionalExample.Framework orElseFramework = nullOptional.orElse(framework);

        assertEquals("Java", orElseFramework.name);

        logger.info("orElse: " + orElseFramework.communityUsers);
    }

    @Test
    @DisplayName("Optional orElseGet")
    public void optional_orElseGet() {

        Optional<OptionalExample.Framework> optionalFramework = Optional.empty();

        Supplier<OptionalExample.Framework> defaultFramework = new Supplier<OptionalExample.Framework>() {

            @Override
            public OptionalExample.Framework get() {
                OptionalExample.Framework framework = new OptionalExample.Framework();
                framework.communityUsers = 200000;
                framework.name = "Java";
                return framework;
            }

        };

        OptionalExample.Framework framework = optionalFramework.orElseGet(defaultFramework);

        assertEquals("Java", framework.name);

        logger.info("orElseGet: " + framework.name);
    }

    @Test
    @DisplayName("optional orElseThrow")
    public void optional_orElseThrow() {

        Optional<OptionalExample.Framework> optionalFramework = Optional.empty();

        logger.info("oeElseThrow: " + optionalFramework.isPresent());

        // will throw exception
        optionalFramework.orElseThrow(IllegalStateException::new);

    }

    @Test
    @DisplayName("optional is empty")
    public void optional_empty() {

        Optional<OptionalExample.Framework> optionalFramework = Optional.empty();

        assertFalse(optionalFramework.isPresent());

        logger.info("isPresent: " + optionalFramework.isPresent());
    }

    @Test
    @DisplayName("optional of")
    public void optional_of() {

        OptionalExample.Framework framework = new OptionalExample.Framework();
        framework.communityUsers = 10000;
        framework.name = "Scala";

        Optional<OptionalExample.Framework> optionalFramework = Optional.of(framework);

        assertEquals("Scala", optionalFramework.get().name);

        logger.info("get: " + optionalFramework.get().communityUsers);
    }

    @Test
    @DisplayName("optional ofNullable")
    public void optional_ofNullable() {

        Optional<OptionalExample.Framework> optionalFramework = Optional.ofNullable(null);

        assertFalse(optionalFramework.isPresent());

        logger.info("ofNullable: " + optionalFramework.isPresent());
    }

    /**
     * Optional Int Examples
     */

    @Test
    @DisplayName("optional int getAsInt")
    public void optional_int_getAsInt() {

        OptionalInt optionalInt = OptionalInt.of(90);

        assertEquals(90, optionalInt.getAsInt(), 0);

        logger.info("getAsInt: " + optionalInt.getAsInt());
    }

    @Test
    @DisplayName("optional int ifPresent ")
    public void optional_int_ifPresent() {

        OptionalInt optionalInt = OptionalInt.of(56);

        optionalInt.ifPresent(p -> System.out.println(p));

        // or

        optionalInt.ifPresent(System.out::println);

        logger.info("int ifPresent: " + optionalInt.getAsInt());
    }

    @Test
    @DisplayName("optional int isPresent")
    public void optional_int_isPresent() {

        OptionalInt optionalInt = OptionalInt.of(56);

        assertTrue(optionalInt.isPresent());

        logger.info("int isPresent: " + optionalInt.getAsInt());
    }

    @Test
    @DisplayName("optional int orElse")
    public void optional_int_orElse() {

        OptionalInt optionalInt = OptionalInt.empty();

        assertEquals(77, optionalInt.orElse(77), 0);

        logger.info("int orElse: " + optionalInt.orElse(77));

    }

    @Test
    @DisplayName("optional int orElseGet - uses IntSupplier")
    public void optional_int_orElseGet() {

        OptionalInt optionalInt = OptionalInt.empty();

        assertEquals(10, optionalInt.orElseGet(() -> 10), 0);

        // or
        IntSupplier intSupplier = new IntSupplier() {
            @Override
            public int getAsInt() {
                return 10;
            }
        };

        assertEquals(10, optionalInt.orElseGet(intSupplier), 0);

        logger.info("orElseGet: " + optionalInt.orElseGet(intSupplier));
    }

    @Test
    public void optional_int_orElseThrow() {

        OptionalInt optionalFramework = OptionalInt.empty();

        optionalFramework.orElseThrow(IllegalStateException::new);
    }

    @Test
    public void optional_int_empty() {

        OptionalInt optionalInt = OptionalInt.empty();

        assertFalse(optionalInt.isPresent());
    }

    @Test
    public void optional_int_of() {

        OptionalInt optionalInt = OptionalInt.of(89);

        assertEquals(89, optionalInt.getAsInt(), 0);
    }

    /**
     * double examples
     */
    @Test
    public void optional_double_getAsDouble() {

        OptionalDouble optionalDouble = OptionalDouble.of(90);

        assertEquals(90, optionalDouble.getAsDouble(), 0);
    }

    @Test
    public void optional_double_ifPresent() {

        OptionalDouble optionalDouble = OptionalDouble.of(56);

        optionalDouble.ifPresent(p -> System.out.println(p));

        // or

        optionalDouble.ifPresent(System.out::println);
    }

    @Test
    public void optional_double_isPresent() {

        OptionalDouble optionalDouble = OptionalDouble.of(56);

        assertTrue(optionalDouble.isPresent());
    }

    @Test
    public void optional_double_orElse() {

        OptionalDouble optionalDouble = OptionalDouble.empty();

        assertEquals(77, optionalDouble.orElse(77), 0);
    }

    @Test
    public void optional_double_orElseGet() {

        OptionalDouble optionalDouble = OptionalDouble.empty();

        assertEquals(10, optionalDouble.orElseGet(() -> 10), 0);

        // or
        DoubleSupplier doubleSupplier = new DoubleSupplier() {
            @Override
            public double getAsDouble() {
                return 10;
            }
        };

        assertEquals(10, optionalDouble.orElseGet(doubleSupplier), 0);
    }

    @Test
    public void optional_double_orElseThrow() {

        OptionalDouble optionalFramework = OptionalDouble.empty();

        optionalFramework.orElseThrow(IllegalStateException::new);
    }

    @Test
    public void optional_double_empty() {

        OptionalDouble optionalDouble = OptionalDouble.empty();

        assertFalse(optionalDouble.isPresent());
    }

    @Test
    public void optional_double_of() {

        OptionalDouble optionalDouble = OptionalDouble.of(89);

        assertEquals(89, optionalDouble.getAsDouble(), 0);
    }

    /**
     * Long example
     */

    @Test
    public void optional_long_getAsLong() {

        OptionalLong optionalLong = OptionalLong.of(90);

        assertEquals(90, optionalLong.getAsLong(), 0);
    }

    @Test
    public void optional_long_ifPresent() {

        OptionalLong optionalLong = OptionalLong.of(56);

        optionalLong.ifPresent(p -> System.out.println(p));

        // or

        optionalLong.ifPresent(System.out::println);
    }

    @Test
    public void optional_long_isPresent() {

        OptionalLong optionalLong = OptionalLong.of(56);

        assertTrue(optionalLong.isPresent());
    }

    @Test
    public void optional_long_orElse() {

        OptionalLong optionalLong = OptionalLong.empty();

        assertEquals(77, optionalLong.orElse(77), 0);
    }

    @Test
    public void optional_long_orElseGet() {

        OptionalLong optionalLong = OptionalLong.empty();

        assertEquals(10, optionalLong.orElseGet(() -> 10), 0);

        // or
        LongSupplier longSupplier = new LongSupplier() {
            @Override
            public long getAsLong() {
                return 10;
            }
        };

        assertEquals(10, optionalLong.orElseGet(longSupplier), 0);
    }

    @Test
    public void optional_long_orElseThrow() {

        OptionalLong optionalFramework = OptionalLong.empty();

        optionalFramework.orElseThrow(IllegalStateException::new);
    }

    @Test
    public void optional_long_empty() {

        OptionalLong optionalLong = OptionalLong.empty();

        assertFalse(optionalLong.isPresent());
    }

    @Test
    public void optional_long_of() {

        OptionalLong optionalLong = OptionalLong.of(89);

        assertEquals(89, optionalLong.getAsLong(), 0);
    }


    /**
     * extra staff
     */
    @Test
    void testExtraOptional() {
        /*
        List<String> carsFiltered = Optional.ofNullable(cars)
            .orElseGet(Collections::emptyList)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList())

         */
    }

    /**
     * We can get rid of all those null checks by utilizing the Java 8 Optional type.
     * The method map accepts a lambda expression of type Function and automatically
     * wraps each function result into an Optional.
     * That enables us to pipe multiple map operations in a row.
     * Null checks are automatically handled under the hood.
     */
    @Test
    @DisplayName("Optional test 1 using Outer")
    void testOptional1() {
        Optional.of(new OptionalExample.Outer())
                .map(OptionalExample.Outer::getNested)
                .map(OptionalExample.Nested::getInner)
                .map(OptionalExample.Inner::getFoo)
                .ifPresent(System.out::println);
    }

    /**
     * An alternative way to achieve the same behavior is by utilizing a supplier
     * function to resolve the nested path:
     */
    @Test
    @DisplayName("Optional test with resolve()")
    void testOptional2() {
        OptionalExample.Outer outer = new OptionalExample.Outer();
        resolve(() -> outer.getNested().getInner().getFoo())
        .ifPresent(System.out::println);

        logger.info("using resolve() : " + resolve(() -> outer.getNested().getInner().getFoo()).toString());
    }

    @Test
    @DisplayName("Test optional ")
    void testOptional3() {
        Optional.of(new OptionalExample.Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);
    }


}
