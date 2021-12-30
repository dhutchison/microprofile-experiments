package com.devwithimagination.microprofile.experiments.config.featureflag;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Object holding the definition of a feature.
 * @author David Hutchison
 */
public class Feature implements Serializable {

    private static final long serialVersionUID = -2229909378701854571L;

    /**
     * The name of the feature
     */
    private String name;

    /**
     * Boolean holding if the feature is enabled or not
     */
    private boolean enabled;

    /**
     * Map holding additional properties which may be considered when resolving if this flag should be enabled or not. 
     */
    private Map<String, String> properties;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return ((this.properties != null) ? this.properties : Collections.emptyMap());
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

}