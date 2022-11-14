package tech.jannotti.billing.core.commons.http.client.exception;

@SuppressWarnings("serial")
public class HttpClientConfigException extends RuntimeException {

    public HttpClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
