package com.devwithimagination.microprofile.experiments.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class EmbeddedPostgresTest {

     // will be started before and stopped after each test method
     @Container
     private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>()
             .withDatabaseName("foo")
             .withUsername("foo")
             .withPassword("secret");

    /**
     * Basic test confirming that no rows are returned from a new table.
     * @throws SQLException if an error occurs accessing the database
     */
    @Test
    public void testDb() throws SQLException {

        /* Configure the database layout first. 
           Ideally this should be done once and not reset for each test */
        Flyway flyway = Flyway.configure()
            .dataSource(postgresqlContainer.getJdbcUrl(), 
                postgresqlContainer.getUsername(), 
                postgresqlContainer.getPassword())
            .locations("classpath:/db/schema")
            .load();
        flyway.migrate();

        /* Do the tests */
        try (Connection connection = postgresqlContainer.createConnection("")) {

            final CarDAO dao = new CarDAO();
            final int actualCount = dao.getNumberOfCars(connection);

            Assertions.assertEquals(0, actualCount, "Not expecting any rows");

        }
    }

}