package com.devwithimagination.microprofile.experiments.config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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
