package tech.jannotti.billing.core.connector.parser;

import tech.jannotti.billing.core.connector.exception.ConnectorException;

@SuppressWarnings("serial")
public class ConnectorParserException extends ConnectorException {

    public ConnectorParserException(String message) {
        super(message);
    }

    public ConnectorParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
