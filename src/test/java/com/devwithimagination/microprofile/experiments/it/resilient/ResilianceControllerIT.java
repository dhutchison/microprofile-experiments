package com.devwithimagination.microprofile.experiments.it.resilient;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.devwithimagination.microprofile.experiments.it.AbstractBaseIT;

import static io.restassured.RestAssured.*;

/**
 * Basic test case for the ResilianceController
 */
public class ResilianceControllerIT extends AbstractBaseIT {

    @Test
    void testTimeoutFallback() throws Exception {
        get("/resilience")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Fallback answer due to timeout"));
    }

}
