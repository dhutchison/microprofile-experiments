package com.devwithimagination.microprofile.experiments.config.featureflag.producer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Annotation for specifying the name of a feature flag to resolve and inject. </p>
 * 
 * <p>
 * This largely based on the ConfigProperty annotation, as we can't reuse it 
 * but it contains the information we would require. 
 * </p>
 * 
 * <p>
 * This is used by the ResolvedFeatureFlagProducer to determine the name
 * of the feature flag to resolve.
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#064;Inject
 * &#064;FeatureProperty(name = "my.long.property")
 * private ResolvedFeatureFlag featureValue;
 * </pre>
 */
@Qualifier
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE })
public @interface FeatureProperty {

    /**
     * The key of the config property used to look up the configuration value.
     *
     * @return Name (key) of the config property to inject
     */
    @Nonbinding
    String name() default "";
}
