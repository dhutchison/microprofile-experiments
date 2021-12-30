package com.devwithimagination.microprofile.experiments.it;

import java.net.URI;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Basic Integration test for confirming the Hello World service works.
 */
public class HelloControllerIT {

    /**
     * The base API url. 
     */
    private static final String BASE_URL = System.getProperty(
            "BASE_URL", "http://localhost:8080/data/");

    /**
     * Basic test case using a Rest Client
     */
    @Test
    public void testSayHello() throws Exception {

        final URI uri = new URI(BASE_URL);

        final HelloControllerClient client = RestClientBuilder.newBuilder()
                .baseUri(uri)
                .build(HelloControllerClient.class);

        final String message = client.sayHello();

        assertEquals("Hello World", message, "Expected messages to match");

    }

}
