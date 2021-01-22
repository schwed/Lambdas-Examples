package com.kon.java.util.misc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;



import java.io.IOException;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class HttpClientExamplesTest {

    private static final Logger logger = Logger.getLogger(HttpClientExamplesTest.class.getName());

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
    @DisplayName("Synchronous Get Request")
    void testSynchRequestGet() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("asynch Request")
    void testAynchRequest() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("Post data")
    void testPostData() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("Basic Auth")
    void testBasicAuth() throws IOException, InterruptedException {

        /*

        // java 11 code
        var client = HttpClient.newHttpClient();

        var request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://postman-echo.com/basic-auth"))
                .build();
        var response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        System.out.println(response1.statusCode());      // 401

        var authClient = HttpClient
                .newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("postman", "password".toCharArray());
                    }
                })
                .build();
        var request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://postman-echo.com/basic-auth"))
                .build();
        var response2 = authClient.send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println(response2.statusCode());      // 200
        */

    }
}


