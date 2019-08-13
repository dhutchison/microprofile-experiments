package com.devwithimagination.microprofile.experiments.it;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/hello")
public interface HelloControllerClient {

    @GET
    public String sayHello();

}