package com.devwithimagination.microprofile.experiments.it.config;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.devwithimagination.microprofile.experiments.it.AbstractBaseIT;

import static io.restassured.RestAssured.*;

/**
 * Integration test for the Configuration feature controller.
 */
public class ConfigTestControllerIT extends AbstractBaseIT {

    /**
     * The message which is expected to be returned when a configuration value is
     * not found.
     */
    private static final String CONFIG_NOT_FOUND_MESSAGE = "Config value not found";

    /**
     * Test to verify the plain property loading appraoch, using injection, works.
     *
     */
    @Test
    void testGetInjectedValue() {

        final String expectedValue = "Config value as Injected by CDI " + "Injected value";

        get("/config/injected")
                .then()
                .assertThat()
                .body(Matchers.equalTo(expectedValue));

    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works.
     */
    @Test
    void testGetLookupValue() {

        final String expectedValue = "Config value from ConfigProvider " + "lookup value";

        get("/config/lookup")
                .then()
                .assertThat()
                .body(Matchers.equalTo(expectedValue));

    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works when a named key is supplied.
     */
    @Test
    void testGetLookupValueForNamedKey() {

        /* Do a test for a key which exists */
        final String expectedValue = "Feature value for feature.three is true";

        given()
                .pathParam("name", "feature.three")
                .get("/config/lookup/{name}")
                .then()
                .assertThat()
                .body(Matchers.equalTo(expectedValue));
    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works when an unknown named key is supplied.
     */
    @Test
    void testGetLookupValueForUnknownNamedKey() {

        /* Do a test for a key which does not exist */
        get("/config/lookup/not-a-real-key")
                .then()
                .assertThat()
                .body(Matchers.equalTo(CONFIG_NOT_FOUND_MESSAGE));
    }

    /**
     * Test to verify that using the Feature Flag Resolver with a known key works as
     * expected.
     */
    @Test
    void testGetResolvedNamedFeatureWithCDI() {

        /*
         * Do a test for a key which exists. For this case we will use one which we know
         * is in the microprofile-config.properties, so is not loaded by our custom
         * config source
         */
        final String expectedValue = "Feature value for future.feature is true";

        get("/config/cdi/header/future.feature")
                .then()
                .assertThat()
                .body(Matchers.equalTo(expectedValue));

    }

    /**
     * Test to verify that using the Feature Flag Resolver with an unknown key works
     * as expected.
     *
     * This differs from the getLookupConfigValue tests as it will default to false
     * for a value which was not found.
     */
    @Test
    void testGetResolvedNamedFeatureWithCDIForUnknownKey() {

        /* Do a test for a key which does not exist */
        get("/config/cdi/header/not-a-real-key")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Feature value for not-a-real-key is false"));
    }

    /**
     * Test to verify the full header based resolving feature works when the role is
     * supplied to make true be returned.
     */
    @Test
    void testGetResolvedBooleanFeatureOneValueWithCDIForAdmin() {

        /* do the test */
        given()
                .header("role", "admin")
                .get("/config/cdi/header-resolved-boolean")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Feature value is true"));
    }

    /**
     * Test to verify the full header based resolving feature works when the role is
     * supplied to make false be returned.
     */
    @Test
    void testGetResolvedBooleanFeatureOneValueWithCDIForOffice() {

        /* do the test */
        given()
                .header("role", "office")
                .get("/config/cdi/header-resolved-boolean")
                .then()
                .assertThat()
                .body(Matchers.equalTo("Feature value is false"));
    }
}
