package com.devwithimagination.microprofile.experiments.config.featureflag;

import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * Implementation of Converter for converting our string representation into the Feature POJO.
 */
public class FeatureConverter implements Converter<Feature> {

    @Override
    public Feature convert(String value) {

        return JsonbBuilder.create().fromJson(value, Feature.class);
    }

}