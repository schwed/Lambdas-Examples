package com.kon.java.util.misc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class LinkedListTest {

    private static final Logger logger = Logger.getLogger(LinkedListTest.class.getName());

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
    @DisplayName("LinkedList test")
    void testLinkedList() {
        LinkedList<Person> list = new LinkedList<>();
        /**
         * add to list:
         * add C2 queue> C2
         * add A3 queue> C2, A3
         * add C4 queue> C2, C4, A3
         * add A1 queue> C2, C4, A3, A1
         */

        Person person = new Person("C", 2);
        list.add(person);
        list.add(new Person("A", 3));

        for (Person p : list) {
            System.out.println(p);
        }

        int idx = list.indexOf(person);

        list.add(idx + 1, new Person("C", 4));

        System.out.println("--------------");
        for (Person p : list) {
            System.out.println(p);
        }

        list.add(new Person("A", 3));
        list.add(new Person("A", 1));

        System.out.println("--------------");
        for (Person p : list){
            System.out.println("list has: " + p);
        }

        /**
         * remove next --> C2 queue> C4, A3, A1
         * remove next --> C4 queue> A3, A1
         */
        System.out.println("removed: " + list.poll());
        System.out.println("removed: " + list.poll());
        list.add(new Person("C", 1));

        System.out.println("--------------");
        for(Person p : list) {
            System.out.println("in lists: " +  p);
        };

        list.poll();
        list.poll();
        list.poll();


    }
}
