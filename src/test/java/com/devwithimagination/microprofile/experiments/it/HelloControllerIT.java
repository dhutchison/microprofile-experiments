package com.devwithimagination.microprofile.experiments.it;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

/**
 * Basic Integration test for confirming the Hello World service works.
 */
public class HelloControllerIT extends AbstractBaseIT {
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
