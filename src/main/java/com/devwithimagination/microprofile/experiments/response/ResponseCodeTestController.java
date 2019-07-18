package com.devwithimagination.microprofile.experiments.response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.github.javafaker.Faker;

/**
 * Controller allowing response codes and sizes to be simulated.
 */
@Path("/response")
public class ResponseCodeTestController {

    /**
     * Controller method to generate different types of response.
     * 
     * @param responseCode the response code to return. A BAD_REQUEST will be
     *                     returned if this parameter is missing
     * @param message      a fixed message body to use. If "messageBytes" is also
     *                     supplied then this parameter will be ignored
     * @param messageBytes a number of bytes to include in the response body. This
     *                     will be extracted from our source text file.
     * @param delay        a number of milliseconds to sleep for during the request
     *                     processing. Note that the total method time will exceed
     *                     this time, but this parameter can be used to make a
     *                     request take longer. This will not be respected if
     *                     validation fails.
     * @param passthroughUrl a url to call as part of this invocation. The response 
     *                      entity from this call will be stored in the passthroughMessage 
     *                      field of the response to this call. This is expected to be
     *                      another call with an endpoint like this one. 
     * @return the response to the call.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRequest(
            @QueryParam("code") final Integer responseCode,
            @QueryParam("message") final String message, 
            @QueryParam("messageBytes") final Integer messageBytes,
            @QueryParam("delay") final Integer delay,
            @QueryParam("passthroughUrl") final URL passthroughUrl) {
        Response response = null;

        /* Validate parameters */
        if (responseCode == null) {
            response = Response.status(Status.BAD_REQUEST).entity("Query parameter 'code' is required.").build();
        } else if (responseCode < 200 || responseCode > 511) {
            response = Response.status(Status.BAD_REQUEST)
                    .entity("Supplied code is not in the supported range (200-511)").build();
        }

        /*
         * If a response is not set at this point, then validation passed. Do our
         * requested work.
         */
        if (response == null) {

            ResponseBuilder responseBuilder = Response.status(responseCode);
            MessageObject entity = new MessageObject();

            if (messageBytes != null) {
                /* Need to make up some message content. */

                Faker faker = new Faker();
                String messageContent = faker.lorem().characters(messageBytes, true);

                entity.setMessage(messageContent);

            } else if (message != null && !message.isEmpty()) {
                entity.setMessage(message);
            }

            if (passthroughUrl != null) {
                /* Call the pass through URL and get it's entity */

                try {
                    URI uri = passthroughUrl.toURI();

                    HttpClient httpClient = HttpClient.newHttpClient();

                    HttpRequest passRequest = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();

                    HttpResponse<String> passResponse = httpClient.send(passRequest, BodyHandlers.ofString());
                    entity.setPassthroughMessage(passResponse.statusCode() + ": " + passResponse.body());

                } catch (URISyntaxException | IOException | InterruptedException e) {
                    /* Change the response */
                    e.printStackTrace();
                    entity = null;

                    responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to contact passthrough API: " + e.getMessage());
                }
                

            }

            /* Only set the entity if we havn't failed */
            if (entity != null && responseCode.intValue() != Status.NO_CONTENT.getStatusCode()) {
                responseBuilder.entity(entity);
            }
            response = responseBuilder.build();

            /* If a delay is required, wait for the requested duration */
            if (delay != null) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    /* Nothing to do here */
                }
            }
        }

        return response;

    }

}