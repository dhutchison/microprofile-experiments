package com.devwithimagination.microprofile.experiments.it;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

/**
 * Basic Integration test for confirming the Hello World service works.
 */
public class HelloControllerIT {

    /**
     * The base API url. 
     */
    private static final String BASE_URL = System.getProperty(
            "BASE_URL", "http://localhost:8080/data/");

    @BeforeAll
    static void setupRestAssured() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Basic test case using a Rest Client
     */
    @Test
    void testSayHello() throws Exception {

        get("/hello")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Hello World"));

    }

}
