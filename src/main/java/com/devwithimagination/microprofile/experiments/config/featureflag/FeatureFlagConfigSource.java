package com.devwithimagination.microprofile.experiments.config.featureflag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * Implementation of ConfigSource for loading feature flag information.
 */
public class FeatureFlagConfigSource implements ConfigSource {

    @Context
    private HttpHeaders headers;

    @Context
    private SecurityContext securityContext;

    @Inject
    private Cache<String, String> cache;

    /**
     * Map of the key to the JSON blob representing the feature definition.
     */
    private final Map<String, String> configurationData;

    public FeatureFlagConfigSource() {

        Jsonb json = JsonbBuilder.create();

        this.configurationData = new HashMap<>();

        /*
         * Load data from the input stream This could be driven by a configuration
         * option somewhere to use a URL instead
         */
        try (InputStream in = FeatureFlagConfigSource.class.getResourceAsStream("/META-INF/feature-flags.json")) {
            Feature[] features = json.fromJson(in, Feature[].class);

            Arrays.stream(features)
                    .forEach((feature) -> this.configurationData.put(feature.getName(), json.toJson(feature)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to configure flag data", e);
        }
    }

    @Override
    public int getOrdinal() {
        return 112;
    }

    @Override
    public Map<String, String> getProperties() {
        System.out.println(
                "In get properties, with headers: " + headers + ", context: " + securityContext + ", cache: " + cache);
        return this.configurationData;
    }

    @Override
    public String getValue(String propertyName) {
        System.out.println(
                "In get value, with headers: " + headers + ", context: " + securityContext + ", cache: " + cache);
        return this.configurationData.get(propertyName);
    }

    @Override
    public String getName() {
        return FeatureFlagConfigSource.class.getSimpleName();
    }

}