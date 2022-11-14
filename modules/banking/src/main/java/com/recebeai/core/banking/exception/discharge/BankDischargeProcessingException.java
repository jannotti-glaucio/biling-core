package tech.jannotti.billing.core.banking.exception.discharge;

@SuppressWarnings("serial")
public class BankDischargeProcessingException extends Exception {

    public BankDischargeProcessingException() {
        super();
    }

    public BankDischargeProcessingException(String message, Object... params) {
        super(String.format(message, params));
    }

}
