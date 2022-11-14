package tech.jannotti.billing.core.connector.rest;

@SuppressWarnings("serial")
public class RestClientException extends RuntimeException {

    public RestClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestClientException(String message, Object... params) {
        super(String.format(message, params));
    }

}
