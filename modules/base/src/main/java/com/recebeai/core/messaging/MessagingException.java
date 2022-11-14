package tech.jannotti.billing.core.messaging;

@SuppressWarnings("serial")
public class MessagingException extends RuntimeException {

    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }

}
