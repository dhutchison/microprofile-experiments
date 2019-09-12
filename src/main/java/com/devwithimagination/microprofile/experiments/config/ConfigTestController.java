package com.devwithimagination.microprofile.experiments.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.NoSuchElementException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import com.devwithimagination.microprofile.experiments.config.featureflag.Feature;
import com.devwithimagination.microprofile.experiments.config.featureflag.resolver.FeatureFlagResolver;
import com.devwithimagination.microprofile.experiments.config.featureflag.producer.FeatureProperty;

/**
 * Implementation of ConfigTestControllerIF. 
 * 
 * Note that when moving all the method level path annotations in to an interface,
 * the class level one needs to stay with the implementation. 
 */
@Path("/config")
@RequestScoped
public class ConfigTestController implements ConfigTestControllerIF {

    /**
     * Message prefix used in response messages
     */
    private static final String RESPONSE_PREFIX = "Feature value for ";

    /**
     * A String value injected through the standard Config feature injection process. 
     */
    @Inject
    @ConfigProperty(name = "injected.value")
    private String injectedValue;

    /**
     * The resolver implementation for turning a Feature in to a boolean
     */
    @Inject
    private FeatureFlagResolver featureFlagResolver;

    /**
     * A Feature value injected through the standard Config feature injection process. This uses the FeatureConverter which was registered with SPI to allow the configuration feature to work out how to turn the string value into the object type requested at the injection target.  
     */
    @Inject
    @ConfigProperty(name = "feature.one")
    private Feature featureOne;

    /**
     * A feature value injected through our custom FeatureProperty/ResolvedFeatureFlagProducer. While our custom provider here uses the configuration feature behind the scenes, we needed to add our own layer to allow headers to be considered before injection. 
     */
    @Inject
    @FeatureProperty(name = "feature.one")
    private boolean resolvedBooleanFeatureOne;

    @Override
    public String getInjectedConfigValue() {
        return "Config value as Injected by CDI " + injectedValue;
    }

    @Override
    public String getLookupConfigValue(final String name) {
        Config config = ConfigProvider.getConfig();

        try {
            Feature feature = config.getValue(name, Feature.class);
            return RESPONSE_PREFIX + feature.getName() + " is " + feature.isEnabled();
        } catch (NoSuchElementException nse) {
            return "Config value not found";
        }
    }

    @Override
    public String getFeatureOneValueWithCDI() {
        if (featureOne != null) {
            return RESPONSE_PREFIX + featureOne.getName() + " is " + featureOne.isEnabled();
        } else {
            return "Config value not found";
        }
    }

    @Override
    public String getResolvedBooleanFeatureOneValueWithCDI() {
        return "Feature value is " + resolvedBooleanFeatureOne;
    }

    @Override
    public String getResolvedNamedFeatureWithCDI(final String name) {

        return RESPONSE_PREFIX + name + " is " + featureFlagResolver.isFeatureEnabled(name);
    }

    @Override
    public String getLookupConfigValue() {
        Config config = ConfigProvider.getConfig();

        String value = config.getValue("value", String.class);
        return "Config value from ConfigProvider " + value;
    }
}
