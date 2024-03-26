package com.devwithimagination.microprofile.experiments.config.featureflag.producer;

import com.devwithimagination.microprofile.experiments.config.featureflag.resolver.FeatureFlagResolver;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

/**
 * Class responsible for producing the resolved boolean representation of a
 * Feature via CDI.
 */
@RequestScoped
public class ResolvedFeatureFlagProducer {

    /**
     * The resolver implementation for turning a Feature in to a boolean
     */
    @Inject
    private FeatureFlagResolver featureFlagResolver;

    /**
     * Based on the target injection point, this will get the name of the feature to
     * load and pass it off to the FeatureFlagResolver for the boolean value to be
     * determined.
     *
     * This expects that the injection point will have been annotated with
     * "@FeatureProperty", if it has not an IllegalStateException will be thrown.
     *
     * @param injectionPoint the target for the calculated value.
     * @return boolean representing the state of the feature toggle. True if the
     *         feature is enabled, false if it is not.
     */
    @Produces
    @FeatureProperty
    public boolean createResolvedFeatureFlagBoolean(InjectionPoint injectionPoint) {

        /* Check we have required items injected */
        if (featureFlagResolver == null) {
            throw new IllegalStateException("FeatureFlagResolver is null!");
        } else if (injectionPoint == null) {
            throw new IllegalStateException("InjectionPoint is null!");
        }

        /* Get the config name annotation value */
        FeatureProperty configProperty = injectionPoint.getAnnotated().getAnnotation(FeatureProperty.class);
        if (configProperty == null) {
            throw new IllegalStateException(
                    "Failed to find required FeatureProperty annotation");
        }

        final String featureName = configProperty.name();

        /* Load the enabled state and return */
        return featureFlagResolver.isFeatureEnabled(featureName);
    }

}
