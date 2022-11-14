package tech.jannotti.billing.core.connector.banking.exception;

import tech.jannotti.billing.core.connector.exception.ConnectorException;

@SuppressWarnings("serial")
public class BankingExchangeException extends ConnectorException {

    public BankingExchangeException(String message, Object... params) {
        super(String.format(message, params));
    }

    public BankingExchangeException(String message, Throwable cause, Object... params) {
        super(String.format(message, params), cause);
    }

}
