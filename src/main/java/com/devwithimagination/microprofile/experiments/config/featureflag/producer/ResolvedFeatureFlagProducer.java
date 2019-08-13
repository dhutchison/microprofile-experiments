package com.devwithimagination.microprofile.experiments.config.featureflag.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.devwithimagination.microprofile.experiments.config.featureflag.FeatureFlagResolver;
/**
 * Class responsible for producing ResolvedFeatureFlag objects via CDI.
 */
@RequestScoped
public class ResolvedFeatureFlagProducer {

    @Inject
    private FeatureFlagResolver featureFlagResolver;

    @Produces
    @FeatureProperty
    public boolean createResolvedFeatureFlagBoolean(InjectionPoint injectionPoint) {

        System.out.println("In createResolvedFeatureFlagBoolean");

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
                    "Attempted to inject ResolvedFeatureFlag without a FeatureProperty annotation");
        }

        final String featureName = configProperty.name();

        /* Load the enabled state and return */
        return featureFlagResolver.isFeatureEnabled(featureName);
    }

}