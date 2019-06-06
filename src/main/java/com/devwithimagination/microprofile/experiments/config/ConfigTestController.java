package com.devwithimagination.microprofile.experiments.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

import com.devwithimagination.microprofile.experiments.config.featureflag.Feature;
import com.devwithimagination.microprofile.experiments.config.featureflag.FeatureFlagResolver;
import com.devwithimagination.microprofile.experiments.config.featureflag.producer.ResolvedFeatureFlag;
import com.devwithimagination.microprofile.experiments.config.featureflag.producer.FeatureProperty;

@Path("/config")
@RequestScoped
public class ConfigTestController {

    @Inject
    @ConfigProperty(name = "injected.value")
    private String injectedValue;

    @Inject
    private FeatureFlagResolver featureFlagResolver;

    @Inject
    @ConfigProperty(name = "feature.one")
    private Feature featureOne;

    @Inject
    @FeatureProperty(name = "feature.one")
    private ResolvedFeatureFlag resolvedFeatureOne;

    @Path("/injected")
    @GET
    public String getInjectedConfigValue() {
        return "Config value as Injected by CDI " + injectedValue;
    }

    @Path("/lookup/{name}")
    @GET
    public String getLookupConfigValue(@PathParam("name") final String name, @Context final Request request) {
        Config config = ConfigProvider.getConfig();

        System.out.println("Request: " + request);

        Feature feature = config.getValue(name, Feature.class);
        if (feature != null) {
            return "Feature value for " + feature.getName() + " is " + feature.isEnabled();
        } else {
            return "Config value not found";
        }
    }

    @Path("/cdi/header")
    @GET
    public String getFeatureOneValueWithCDI() {
        if (featureOne != null) {
            return "Feature value for " + featureOne.getName() + " is " + featureOne.isEnabled();
        } else {
            return "Config value not found";
        }
    }

    @Path("/cdi/header-resolved")
    @GET
    public String getResolvedFeatureOneValueWithCDI() {
        if (resolvedFeatureOne != null) {
            return "Feature value for " + resolvedFeatureOne.getFeatureName() + " is " + resolvedFeatureOne.isEnabled();
        } else {
            return "Config value not found";
        }
    }

    @Path("/cdi/header/{name}")
    @GET
    public String getResolvedNamedFeatureWithCDI(@PathParam("name") final String name) {

        return "Feature value for " + name + " is " + featureFlagResolver.isFeatureEnabled(name);
    }

    @Path("/lookup")
    @GET
    public String getLookupConfigValue() {
        Config config = ConfigProvider.getConfig();

        String value = config.getValue("value", String.class);
        return "Config value from ConfigProvider " + value;
    }
}
