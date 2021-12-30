package com.devwithimagination.microprofile.experiments.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Basic test case showing how Testcontainers can be used to connect to a database. 
 */
@Testcontainers
class EmbeddedPostgresTest {

    // will be shared between test methods
    @SuppressWarnings("rawtypes")
    @Container
    private static final PostgreSQLContainer DB_CONTAINER = new PostgreSQLContainer("postgres:11.7");

    private DataSource ds;

    @BeforeAll
    static void setupContainer() {
        final Flyway flyway = Flyway.configure()
                .dataSource(DB_CONTAINER.getJdbcUrl(), DB_CONTAINER.getUsername(), DB_CONTAINER.getPassword())
                .locations("classpath:db/schema")
                .load();
        flyway.migrate();
    }

    @BeforeEach
    void setupDataSource() {
        final PGSimpleDataSource postgresDs = new PGSimpleDataSource();
        postgresDs.setURL(DB_CONTAINER.getJdbcUrl());
        postgresDs.setUser(DB_CONTAINER.getUsername());
        postgresDs.setPassword(DB_CONTAINER.getPassword());

        this.ds = postgresDs;
    }

    /**
     * Basic test confirming that no rows are returned from a new table.
     * @throws SQLException if an error occurs accessing the database
     */
    @Test
    void testDb() throws SQLException {

        try (Connection connection = ds.getConnection()) {

            final CarDAO dao = new CarDAO();
            final int actualCount = dao.getNumberOfCars(connection);

            assertEquals(0, actualCount, "Not expecting any rows");
        }
    }

}
