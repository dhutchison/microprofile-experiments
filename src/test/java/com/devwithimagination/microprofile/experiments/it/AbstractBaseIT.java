package com.devwithimagination.microprofile.experiments.it;

import java.io.File;
import java.io.IOException;

import org.jacoco.core.tools.ExecDumpClient;
import org.jacoco.core.tools.ExecFileLoader;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BindMode;
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
        /* Expose to containers a bound port Jaeger if we are running it locally */
        Testcontainers.exposeHostPorts(4317);

        API_CONTAINER = new GenericContainer<>(DockerImageName.parse("devwithimagination/microprofile-experiments:latest"))
                .withExposedPorts(8080, 6300)
                .waitingFor(Wait.forHttp("/health").forStatusCode(200))
                /*
                 * I've not implemented a healthcheck, but there is a strategy for waiting for the container to be healthy,
                 * but in this case they are the same end goal
                 */
                .withFileSystemBind("./target/jacoco-agent", "/opt/jacoco/agent", BindMode.READ_ONLY)
                .withFileSystemBind("./target/otel-agent-extensions", "/opt/otel-agent-extensions", BindMode.READ_ONLY)
                // regex-class-instrumentation.jar
                .withEnv("OTEL_EXPORTER_OTLP_ENDPOINT", "http://host.testcontainers.internal:4317")
                .withEnv("OTEL_EXPORTER_OTLP_PROTOCOL", "grpc")
                .withEnv("JAVA_OPTS",
                        "-javaagent:/opt/jacoco/agent/org.jacoco.agent-runtime.jar=output=tcpserver,address=*,port=6300 -Dotel.javaagent.extensions=/opt/otel-agent-extensions")
                .withEnv("PAYARA_OPTS", "--noHazelcast")
                .withReuse(true);

        API_CONTAINER.start();

        /* Setup the base URL for RestAssured */
        final String baseUrl = System.getProperty(
                "BASE_URL", "http://" + API_CONTAINER.getHost() + ":" + API_CONTAINER.getMappedPort(8080) + "/experiments/data/");

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
        ExecDumpClient jacocoClient = new ExecDumpClient();
        ExecFileLoader dump = jacocoClient.dump(API_CONTAINER.getHost(), API_CONTAINER.getMappedPort(6300));
        dump.save(new File("./target/coverage-reports/jacoco-it.exec"), true);

        System.out.println(API_CONTAINER.getLogs());
    }
}
