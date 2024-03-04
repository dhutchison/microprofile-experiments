package com.devwithimagination.microprofile.experiments.config;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * Interface defining the endpoints for working with the
 * Configuration feature.
 */
public interface ConfigTestControllerIF {

    @Path("/injected")
    @GET
    public String getInjectedConfigValue();

    @Path("/lookup/{name}")
    @GET
    public String getLookupConfigValue(@PathParam("name") final String name);

    @Path("/cdi/header")
    @GET
    public String getFeatureOneValueWithCDI();

    @Path("/cdi/header-resolved-boolean")
    @GET
    public String getResolvedBooleanFeatureOneValueWithCDI();

    @Path("/cdi/header/{name}")
    @GET
    public String getResolvedNamedFeatureWithCDI(@PathParam("name") final String name);

    @Path("/lookup")
    @GET
    public String getLookupConfigValue();
}
