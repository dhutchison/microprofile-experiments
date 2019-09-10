package com.devwithimagination.microprofile.experiments.it.config;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.ClientRequestFilter;

import com.devwithimagination.microprofile.experiments.config.ConfigTestControllerIF;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test for the Configuration feature controller.
 */
public class ConfigTestControllerIT {

    /**
     * The base API url.
     */
    private static final String BASE_URL = System.getProperty("BASE_URL", "http://localhost:8080/data/");

    /**
     * The message which is expected to be returned when a configuration value is
     * not found.
     */
    private static final String CONFIG_NOT_FOUND_MESSAGE = "Config value not found";

    /**
     * The URI as the base of the API
     */
    private URI uri;

    /**
     * Setup pre-requisites to tests.
     * 
     * @throws URISyntaxException if the base URL is malformed
     */
    @Before
    public void setup() throws URISyntaxException {
        /*
         * As we are using the interface which is implemented server side in this case,
         * the class level path annotation cannot be in the interface.
         * 
         * See:
         * https://stackoverflow.com/questions/16950873/is-it-possible-to-define-a-jax-
         * rs-service-interface-separated-from-its-implement
         */
        uri = new URI(BASE_URL + "/config");
    }

    /**
     * Test to verify the plain property loading appraoch, using injection, works.
     * 
     */
    @Test
    public void testGetInjectedValue() {

        final String expectedValue = "Config value as Injected by CDI " + "Injected value";

        ConfigTestControllerIF client = getConfiguredClient(null);

        String message = client.getInjectedConfigValue();

        Assert.assertEquals("Expected injected message to match", expectedValue, message);

    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works.
     */
    @Test
    public void testGetLookupValue() {

        final String expectedValue = "Config value from ConfigProvider " + "lookup value";

        ConfigTestControllerIF client = getConfiguredClient(null);

        String message = client.getLookupConfigValue();

        Assert.assertEquals("Expected lookup message to match", expectedValue, message);

    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works when a named key is supplied.
     */
    @Test
    public void testGetLookupValueForNamedKey() {

        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(null);

        /* Do a test for a key which exists */
        final String expectedValue = "Feature value for feature.three is true";
        final String message = client.getLookupConfigValue("feature.three");

        Assert.assertEquals("Expected lookup message to match", expectedValue, message);
    }

    /**
     * Test to verify the plain property loading approach, using the config store
     * instance directly, works when an unknown named key is supplied.
     */
    @Test
    public void testGetLookupValueForUnknownNamedKey() {

        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(null);

        /* Do a test for a key which does not exist */
        final String message = client.getLookupConfigValue("not-a-real-key");

        Assert.assertEquals("Expected lookup message to match", CONFIG_NOT_FOUND_MESSAGE, message);
    }

    /**
     * Test to verify that using the Feature Flag Resolver with a known key works as
     * expected.
     */
    @Test
    public void testGetResolvedNamedFeatureWithCDI() {
        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(null);

        /*
         * Do a test for a key which exists. For this case we will use one which we know
         * is in the microprofile-config.properties, so is not loaded by our custom
         * config source
         */
        final String expectedValue = "Feature value for future.feature is true";
        final String message = client.getResolvedNamedFeatureWithCDI("future.feature");

        Assert.assertEquals("Expected lookup message to match", expectedValue, message);
    }

    /**
     * Test to verify that using the Feature Flag Resolver with an unknown key works
     * as expected.
     * 
     * This differs from the getLookupConfigValue tests as it will default to false
     * for a value which was not found.
     */
    @Test
    public void testGetResolvedNamedFeatureWithCDIForUnknownKey() {
        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(null);

        /* Do a test for a key which does not exist */
        final String message = client.getResolvedNamedFeatureWithCDI("not-a-real-key");

        Assert.assertEquals("Expected lookup message to match", "Feature value for not-a-real-key is false", message);
    }

    /**
     * Test to verify the full header based resolving feature works when the role is
     * supplied to make true be returned.
     */
    @Test
    public void testGetResolvedBooleanFeatureOneValueWithCDIForAdmin() {

        /* Setup the header */
        ClientRequestFilter requestFilter = requestContext -> {
            requestContext.getHeaders().add("role", "admin");
        };

        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(requestFilter);

        /* do the test */
        final String message = client.getResolvedBooleanFeatureOneValueWithCDI();
        Assert.assertEquals("Expected lookup message to match", "Feature value is true", message);
    }

    /**
     * Test to verify the full header based resolving feature works when the role is
     * supplied to make false be returned.
     */
    @Test
    public void testGetResolvedBooleanFeatureOneValueWithCDIForOffice() {

        /* Setup the header */
        ClientRequestFilter requestFilter = requestContext -> {
            requestContext.getHeaders().add("role", "office");
        };

        /* Get the client */
        ConfigTestControllerIF client = getConfiguredClient(requestFilter);

        /* do the test */
        final String message = client.getResolvedBooleanFeatureOneValueWithCDI();
        Assert.assertEquals("Expected lookup message to match", "Feature value is false", message);
    }

    /**
     * Method to get a configured client to the API.
     * 
     * @param clientRequestFilter an optional client request filter to register with
     *                            the rest client.
     */
    private ConfigTestControllerIF getConfiguredClient(ClientRequestFilter clientRequestFilter) {

        /* Setup the builder */
        final RestClientBuilder builder = RestClientBuilder.newBuilder();

        if (clientRequestFilter != null) {
            /* Register the client filter */
            builder.register(clientRequestFilter);
        }

        /* Build the client */
        ConfigTestControllerIF client = builder.baseUri(uri).build(ConfigTestControllerIF.class);

        return client;
    }
}