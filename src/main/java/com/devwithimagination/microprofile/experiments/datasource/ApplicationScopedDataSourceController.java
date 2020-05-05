package com.devwithimagination.microprofile.experiments.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST API which will return the number of current database connections which 
 * a target postgres server reports as in use. 
 * 
 * This will include connections of all states. 
 */
@Path("/datasource")
@DataSourceDefinition(
    name = "java:app/jdbc/ExampleDS",
    className = "org.postgresql.ds.PGConnectionPoolDataSource",
    serverName = "localhost",
    portNumber = 5432,
    user = "postgres",
    databaseName = "postgres",
    password = "example",
    maxPoolSize = 100,
    initialPoolSize = 20
)
@ApplicationScoped
public class ApplicationScopedDataSourceController {

    @Resource(lookup="java:app/jdbc/ExampleDS")
    private DataSource exampleDS;

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(ApplicationScopedDataSourceController.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadStats() {

        Response response;
        
        final String sql = 
            "SELECT usename, COUNT(*) AS conns " + 
            "FROM pg_stat_activity " + 
            "WHERE datname='postgres' " + 
            "GROUP BY usename";
        try (Connection connection = exampleDS.getConnection()) {

            final Map<String, String> userConnections = new HashMap<>();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                try (ResultSet results = stmt.executeQuery()) {
                    
                    while(results.next()) {
                        userConnections.put(
                            results.getString("usename"),
                            results.getString("conns")
                        );
                    }
                }
            }

            response = Response.ok(userConnections).build();

        } catch (Exception e) {
            logger.error("Failed to get database statistics", e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to get database statistics: " + e.getMessage())
                .build();

        }

        return response;
    }

}