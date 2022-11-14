package tech.jannotti.billing.core.connector.exception;

@SuppressWarnings("serial")
public class ConnectorNotFoundException extends ConnectorException {

    public ConnectorNotFoundException(String message) {
        super(message);
    }

}
