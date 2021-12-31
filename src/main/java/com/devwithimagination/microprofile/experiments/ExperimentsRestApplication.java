package com.devwithimagination.microprofile.experiments;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

import com.devwithimagination.microprofile.experiments.config.ConfigTestController;
import com.devwithimagination.microprofile.experiments.metric.MetricController;
import com.devwithimagination.microprofile.experiments.resilient.ResilienceController;
import com.devwithimagination.microprofile.experiments.response.ResponseCodeTestController;
import com.devwithimagination.microprofile.experiments.secure.ProtectedController;

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
        // classes.add(PetController.class);
        classes.add(ResilienceController.class);
        classes.add(ResponseCodeTestController.class);
        classes.add(ProtectedController.class);

        return classes;
    }

}
