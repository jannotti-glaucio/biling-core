package tech.jannotti.billing.core.validation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
