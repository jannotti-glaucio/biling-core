package tech.jannotti.billing.core.rest.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ControllerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(String message, Throwable cause, Object... params) {
        super(String.format(message, params), cause);
    }

    public ControllerException(String message, Object... params) {
        super(String.format(message, params));
    }

}