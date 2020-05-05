package com.devwithimagination.microprofile.experiments.it.datasource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the Application Scoped Datasource controller.
 */
public class ApplicationScopedDataSourceControllerIT {

    /**
     * The base API url.
     */
    private static final String BASE_URL = System.getProperty("BASE_URL", "http://localhost:8080/data");

    /**
     * The URI as the base of the API
     */
    private URI uri;

    /**
     * Setup pre-requisites to tests.
     * 
     * @throws URISyntaxException if the base URL is malformed
     */
    @BeforeEach
    public void setup() throws URISyntaxException {
        /*
         * As we are using the interface which is implemented server side in this case,
         * the class level path annotation cannot be in the interface.
         * 
         * See:
         * https://stackoverflow.com/questions/16950873/is-it-possible-to-define-a-jax-
         * rs-service-interface-separated-from-its-implement
         */
        uri = new URI(BASE_URL + "/datasource");
    }

    /**
     * Test to verify that getting connection statistics from the datasource works.
     * 
     * @throws IOException if an error occurs calling the API endpoint
     * 
     */
    @Test
    public void testGetStatistics() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();

        /* Synchronous request */
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        /* Check we got an OK response code */
        Assertions.assertEquals( 
            Status.OK.getStatusCode(), response.statusCode(),
            "Expected OK response code");

    }

}