package tech.jannotti.billing.core.connector.exception;

@SuppressWarnings("serial")
public class ConnectorException extends RuntimeException {

    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorException(String message, Object... params) {
        super(String.format(message, params));
    }

}
