package fr.hedwin.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    public Response(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }

    public static enum ResponseType{
        @JsonProperty("error") ERROR,  @JsonProperty("success") SUCCESS;
    }

    private final ResponseType responseType;
    private final String message;

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getMessage() {
        return message;
    }
}
