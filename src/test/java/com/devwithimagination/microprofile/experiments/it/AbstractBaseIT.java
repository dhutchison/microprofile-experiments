package com.devwithimagination.microprofile.experiments.it;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import io.restassured.RestAssured;

/**
 * Abstract base class for our integration tests.
 *
 * This exists to ensure that we only create test containers once and reuse them for all integration tests.
 */
public abstract class AbstractBaseIT {

    static final GenericContainer<?> API_CONTAINER;

    static {
        API_CONTAINER = new GenericContainer<>(DockerImageName.parse("devwithimagination/microprofile-experiments:latest"))
                .withExposedPorts(8080)
                .waitingFor(Wait.forHttp("/health").forStatusCode(200));
        // I've not implemented a healthcheck, but there is a strategy for waiting for the container to be healthy

        API_CONTAINER.start();

        /* Setup the base URL for RestAssured */
        final String baseUrl = System.getProperty(
                "BASE_URL", "http://" + API_CONTAINER.getHost() + ":" + API_CONTAINER.getMappedPort(8080) + "/experiments/data/");

        System.out.println(baseUrl);

        RestAssured.baseURI = baseUrl;
    }
}
