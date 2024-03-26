package com.devwithimagination.microprofile.experiments.config.featureflag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * Implementation of ConfigSource for loading feature flag information.
 *
 * This implementation will load from a file containing a JSON array, but
 * equally could have performed an HTTP call to get this JSON array.
 */
public class FeatureFlagConfigSource implements ConfigSource {

    /**
     * Map of the key to the JSON blob representing the feature definition.
     */
    private final Map<String, String> configurationData;

    /**
     * Create a new FeatureFlagConfigSource.
     *
     * This will read the configuration file in as part of the object construction.
     */
    public FeatureFlagConfigSource() {

        Jsonb json = JsonbBuilder.create();

        try {
            this.configurationData = new HashMap<>();

            /*
             * Load data from the input stream This could be driven by a configuration
             * option somewhere to use a URL instead
             */
            try (InputStream in = FeatureFlagConfigSource.class.getResourceAsStream("/META-INF/feature-flags.json")) {
                Feature[] features = json.fromJson(in, Feature[].class);

                Arrays.stream(features)
                        .forEach(feature -> this.configurationData.put(feature.getName(), json.toJson(feature)));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to configure flag data", e);
            }
        } finally {
            try {
                json.close();
            } catch (Exception e) {
                /* Nothing to do here! */
            }
        }
    }

    @Override
    public int getOrdinal() {
        return 112;
    }

    @Override
    public Map<String, String> getProperties() {

        return this.configurationData;
    }

    @Override
    public String getValue(String propertyName) {

        return this.configurationData.get(propertyName);
    }

    @Override
    public String getName() {
        return FeatureFlagConfigSource.class.getSimpleName();
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.configurationData.keySet();
    }

}
