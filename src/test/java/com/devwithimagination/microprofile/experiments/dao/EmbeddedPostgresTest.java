package com.devwithimagination.microprofile.experiments.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class EmbeddedPostgresTest {

    @Rule 
    public PreparedDbRule db =
        EmbeddedPostgresRules.preparedDatabase(
            FlywayPreparer.forClasspathLocation("db/schema"));


    /**
     * Basic test confirming that no rows are returned from a new table.
     * @throws SQLException if an error occurs accessing the database
     */
    @Test
    public void testDb() throws SQLException {
        DataSource datasource = db.getTestDatabase();

        try (Connection connection = datasource.getConnection()) {

            final String sql = 
                "SELECT * FROM car";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                try (ResultSet results = statement.executeQuery()) {

                    Assert.assertFalse("Not expecting any rows", results.next());

                }

            }


        }
    }

}