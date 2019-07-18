package com.devwithimagination.microprofile.experiments.response;

/**
 * Wrapper object for the message which will be returned from the response code
 * test controller.
 */
public class MessageObject {

    /**
     * The message returned by the service.
     */
    private String message;

    /**
     * If a pass through call was specified when calling the API, this object will
     * contain the response from the downstream call.
     */
    private String passthroughMessage;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the passthroughMessage
     */
    public String getPassthroughMessage() {
        return passthroughMessage;
    }

    /**
     * @param passthroughMessage the passthroughMessage to set
     */
    public void setPassthroughMessage(String passthroughMessage) {
        this.passthroughMessage = passthroughMessage;
    }

}