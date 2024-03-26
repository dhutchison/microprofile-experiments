package com.devwithimagination.microprofile.experiments;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.auth.LoginConfig;

import com.devwithimagination.microprofile.experiments.config.ConfigTestController;
import com.devwithimagination.microprofile.experiments.metric.MetricController;
import com.devwithimagination.microprofile.experiments.resilient.ResilienceController;
import com.devwithimagination.microprofile.experiments.response.ResponseCodeTestController;
import com.devwithimagination.microprofile.experiments.secure.ProtectedController;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/data")
@ApplicationScoped

@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({ "protected" })

public class ExperimentsRestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        final Set<Class<?>> classes = new HashSet<>();
        classes.addAll(super.getClasses());

        classes.add(ConfigTestController.class);
        classes.add(HelloController.class);
        classes.add(MetricController.class);
        classes.add(ResilienceController.class);
        classes.add(ResponseCodeTestController.class);
        classes.add(ProtectedController.class);

        return classes;
    }

}
