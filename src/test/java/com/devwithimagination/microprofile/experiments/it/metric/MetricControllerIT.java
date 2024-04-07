package com.devwithimagination.microprofile.experiments.it.metric;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.devwithimagination.microprofile.experiments.it.AbstractBaseIT;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

/**
 * Basic test case for the MetricController
 */
public class MetricControllerIT extends AbstractBaseIT {

    @Test
    void testTimedRequest() throws Exception {
        get("/metric/timed")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Request is used in statistics, check with the Metrics call."));
    }

    @Test
    void testCounter() throws Exception {

        final String path = "metric/increment";

        /* Call first to get the initial value */
        Response initialResponse = get(path);
        final int initialValue = Integer.parseInt(initialResponse.asString());

        /* Do a second call and ensure incremented */
        get(path)
                .then()
                .assertThat()
                .body(Matchers.equalTo(Integer.toString(initialValue + 1)));

    }

}
