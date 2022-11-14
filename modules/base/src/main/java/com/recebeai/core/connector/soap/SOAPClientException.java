package tech.jannotti.billing.core.connector.soap;

@SuppressWarnings("serial")
public class SOAPClientException extends RuntimeException {

    public SOAPClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
