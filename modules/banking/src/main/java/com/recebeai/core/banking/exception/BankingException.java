package tech.jannotti.billing.core.banking.exception;

@SuppressWarnings("serial")
public class BankingException extends RuntimeException {

    public BankingException() {
        super();
    }

    public BankingException(String message, Object... params) {
        super(String.format(message, params));
    }

}
