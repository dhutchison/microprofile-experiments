package com.devwithimagination.microprofile.experiments.config.featureflag.producer;


/**
 * Object holding the resolved details of a feature flag. 
 */
public class ResolvedFeatureFlag {

    private final String featureName;
    private final boolean enabled;

    ResolvedFeatureFlag(final String featureName, final boolean enabled) {
        this.featureName = featureName;
        this.enabled = enabled;
    }

    /**
     * @return the featureName
     */
    public String getFeatureName() {
        return featureName;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

}