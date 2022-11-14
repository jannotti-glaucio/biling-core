package tech.jannotti.billing.core.services.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, Object... params) {
        super(String.format(message, params), cause);
    }

    public ServiceException(String message, Object... params) {
        super(String.format(message, params));
    }

}
