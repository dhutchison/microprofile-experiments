package com.devwithimagination.microprofile.experiments.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simple DAO for loading information on cars from our database.
 */
public class CarDAO {

    /**
     * Get the number of cars held in the database.
     * 
     * @param connection the database connection to use.
     * @return the number of car records found.
     * @throws SQLException if an error occurs while querying the database.
     */
    public int getNumberOfCars(final Connection connection) throws SQLException {

        int count = 0;

        final String sql = "SELECT COUNT(1) AS numRecords FROM car";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet results = statement.executeQuery()) {

                if (results.next()) {
                    /* Only expecting a single row */
                    count = results.getInt("numRecords");
                }

            }

        }

        return count;
    }

}