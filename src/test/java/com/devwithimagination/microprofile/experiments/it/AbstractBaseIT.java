package com.devwithimagination.microprofile.experiments.it;

import java.io.File;
import java.io.IOException;

import org.jacoco.core.tools.ExecDumpClient;
import org.jacoco.core.tools.ExecFileLoader;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.restassured.RestAssured;

/**
 * Abstract base class for our integration tests.
 *
 * This exists to ensure that we only create test containers once and reuse them for all integration tests.
 */
public abstract class AbstractBaseIT {

    static final DockerComposeContainer<?> COMPOSE_CONTAINER;

    static {
        /* Expose to containers a bound port Jaeger if we are running it locally */
        Testcontainers.exposeHostPorts(4317);

        COMPOSE_CONTAINER = new DockerComposeContainer<>(new File("docker-compose.yml"))
                .withExposedService("api", 8080, Wait.forHttp("/health").forStatusCode(200))
                .withExposedService("api", 6300);

        COMPOSE_CONTAINER.start();

        /* Setup the base URL for RestAssured */
        ContainerState apiContainer = COMPOSE_CONTAINER.getContainerByServiceName("api").get();
        final String baseUrl = System.getProperty(
                "BASE_URL", "http://" + apiContainer.getHost() + ":" + apiContainer.getMappedPort(8080) + "/experiments/data/");

        System.out.println(baseUrl);

        RestAssured.baseURI = baseUrl;
    }

    @AfterAll
    static void captureCoverage() throws IOException {
        /*
         * This is calling after each instance of this subclass, not after every single test.
         * I think the better way to do this will be jacoco in tcpserver mode and AfterAll calling the
         * command to dump & append to the existing file.
         *
         * This file will be cleaned as part of the maven cleanup before a new run
         *
         * Then we have called it before the container is automatically exited.
         */

        ContainerState apiContainer = COMPOSE_CONTAINER.getContainerByServiceName("api").get();

        ExecDumpClient jacocoClient = new ExecDumpClient();
        ExecFileLoader dump = jacocoClient.dump(apiContainer.getHost(), apiContainer.getMappedPort(6300));
        dump.save(new File("./target/coverage-reports/jacoco-it.exec"), true);

        System.out.println(apiContainer.getLogs());
    }
}
