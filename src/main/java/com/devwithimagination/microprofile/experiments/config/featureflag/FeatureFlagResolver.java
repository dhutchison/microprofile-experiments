package com.devwithimagination.microprofile.experiments.config.featureflag;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.eclipse.microprofile.config.Config;

/**
 * Injectable class which is designed to resolve the value of a feature flag
 * based on a header.
 * 
 */
@RequestScoped
public class FeatureFlagResolver {

    @Context
    private HttpHeaders headers;

    @Inject
    private Config config;

    public boolean isFeatureEnabled(String featureName) {

        /* Check that expected configuration is available */
        if (headers == null) {
            throw new IllegalStateException("No header information available");
        }
        if (config == null) {
            throw new IllegalStateException("No config information available");
        }

        /* Get the feature flag */
        /* A safer implementation of this could check if the configuration
         * value is already a boolean, and only perform the additional resolve 
         * behaviour if the configuration value was a feature. 
         * Using this approach would make it easier to override 
         * features through the higher ordinal ConfigSources, like 
         * system properties and environment variables. 
         */
        final Feature feature = config.getValue(featureName, Feature.class);

        /* Work out if we should return it as being enabled or not. */
        final boolean resolvedValue;
        if (feature == null) {
            /* Feature definition not found, default to false */
            resolvedValue = false;
        } else {
            /* Got a definition value, resolve it */
            resolvedValue = resolve(feature);
        }

        return resolvedValue;
    }

    /**
     * Basic method demonstrating how a feature flag could be resolved.
     * 
     * This would likely use a Strategy Pattern if implemented fully.
     * 
     * @param feature the feature to resolve the state for.
     * @return true if the feature is to be enabled, otherwise false.
     */
    private boolean resolve(final Feature feature) {

        boolean enabled = feature.isEnabled();

        /* Only need to do further evaluation if the feature is actually enabled */
        if (enabled) {

            /*
             * Check if there are any properties which need considered when deciding if this
             * is enabled or not
             */
            if (!feature.getProperties().isEmpty()) {

                /*
                 * Simple implementation, look for a single item to check what a "role" header
                 * should be
                 */
                final String requiredRole = feature.getProperties().get("requires-header-role");

                if (requiredRole != null) {
                    /*
                     * If a role is required, only return that the feature is enabled if the header
                     * exists and has the same value
                     */
                    List<String> roleHeaderValues = headers.getRequestHeader("role");

                    enabled = (roleHeaderValues != null && roleHeaderValues.contains(requiredRole));
                }

            }

        }
        return enabled;
    }

}