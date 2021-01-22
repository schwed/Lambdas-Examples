package com.kon.java.util.misc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class FilesTest {

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
    @DisplayName("Files test Walk")
    void testWalk() throws IOException {
        Path start = Paths.get("..");
        int maxDepth = 5;
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".txt"))
                    .collect(Collectors.joining("\n"));
            System.out.println("walk(): \n" + joined);
        }
    }

    @Test
    @DisplayName("Files test Find")
    void testFind() throws IOException {
        Path start = Paths.get("..");
        int maxDepth = 5;
        try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) ->
                String.valueOf(path).endsWith(".txt"))) {
            String joined = stream
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n"));
            System.out.println("find(): \n" + joined);
        }
    }

    @Test
    @DisplayName("Files test List")
    void testList() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(".."))) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(path -> !path.startsWith("."))
                    .sorted()
                    .collect(Collectors.joining("\n"));
            System.out.println("list(): " + joined);
        }
    }

    @Test
    @DisplayName("Files test Lines")
    void testLines() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("../dummy.txt"))) {
            stream
                    .filter(line -> line.contains("ETH"))
                    .map(String::trim)
                    .forEach(System.out::println);
        }
    }

    @Test
    @DisplayName("Files test Reader")
    void testReader() throws IOException {
        Path path = Paths.get("../dummy.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            System.out.println(reader.readLine()); // will read first line in a file
        }
    }

    @Disabled
    @Test
    @DisplayName("Files test Writer")
    void testWriter() throws IOException {
        Path path = Paths.get("../testing.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("print('Hello World');");
        }
    }

    @Test
    @DisplayName("Files test Read Write Line")
    void testReadWriteLines() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("..dummy.txt"));
        lines.add("print('foobar');");
        Files.write(Paths.get("..", "dummy.txt"), lines);
    }

    @Test
    @DisplayName("Files test Reader Lines")
    void testReaderLines() throws IOException {
        Path path = Paths.get("../dummy.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            long countPrints = reader
                    .lines()
                    .filter(line -> line.contains("print"))
                    .count();
            System.out.println(countPrints);
        }
    }

}
