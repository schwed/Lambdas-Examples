package com.kon.java.util;

import com.kon.java.util.function.FunctionExampleTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DoubleSummaryStatisticsExampleTest {


    private static final Logger logger = Logger.getLogger(FunctionExampleTest.class.getName());

    List<DoubleSummaryStatisticsExample.Company> companies;

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
        companies = new ArrayList<>();
        companies.add(new DoubleSummaryStatisticsExample.Company(100.12));
        companies.add(new DoubleSummaryStatisticsExample.Company(142.65));
        companies.add(new DoubleSummaryStatisticsExample.Company(12.1));
        companies.add(new DoubleSummaryStatisticsExample.Company(184.90));
    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");
        companies.clear();
        companies = null;
    }

    @Test
    @DisplayName("double summary stats with stream")
    public void double_summary_stats_with_stream() {
        DoubleSummaryStatistics stats = companies.stream()
                .mapToDouble(DoubleSummaryStatisticsExample.Company::getRevenue).summaryStatistics();

        logger.info(stats.toString());
        // average
        assertEquals(109.9425, stats.getAverage(), 0);

        // count
        assertEquals(4, stats.getCount(), 0);

        // max
        assertEquals(184.9, stats.getMax(), 0);

        // min
        assertEquals(12.1, stats.getMin(), 0);

        // sum
        assertEquals(439.77, stats.getSum(), 0);
    }

    @Test
    @DisplayName("double summary stats stream reduction target")
    public void double_summary_stats_stream_reduction_target() {

        DoubleSummaryStatistics stats = companies.stream().collect(
                Collectors.summarizingDouble(DoubleSummaryStatisticsExample.Company::getRevenue));

        logger.info(stats.toString());
        // average
        assertEquals(109.9425, stats.getAverage(), 0);

        // count
        assertEquals(4, stats.getCount(), 0);

        // max
        assertEquals(184.9, stats.getMax(), 0);

        // min
        assertEquals(12.1, stats.getMin(), 0);

        // sum
        assertEquals(439.77, stats.getSum(), 0);
    }

}
