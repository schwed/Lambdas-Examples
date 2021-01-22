package com.kon.java.util.function;

import com.google.common.collect.Lists;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.kon.java.util.function.FunctionExample.mapPersonToJob;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FunctionExampleTest {

    private static final Logger logger = Logger.getLogger(FunctionExampleTest.class.getName());

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
    public void map_objects_with_java8_function() {

        List<FunctionExample.Person> persons = Lists.newArrayList(
                new FunctionExample.Person(1, "Husband"),
                new FunctionExample.Person(2, "Dad"),
                new FunctionExample.Person(3, "Software engineer"),
                new FunctionExample.Person(4, "Adjunct instructor"),
                new FunctionExample.Person(5, "Pepperoni hanger")
        );

        List<FunctionExample.Job> jobs = persons.stream().map(mapPersonToJob)
                .collect(Collectors.toList());

        logger.info(String.valueOf(jobs));

        assertEquals(5, jobs.size());
    }

    @Test
    public void apply() {

        FunctionExample.Person person = new FunctionExample.Person(1, "Description");

        FunctionExample.Job job = mapPersonToJob.apply(person);

        assertEquals("Description", job.getDescription());
    }



}
