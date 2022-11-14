package tech.jannotti.billing.core.commons.bean.parser;

@SuppressWarnings("serial")
public class BeanParserException extends RuntimeException {

    public BeanParserException(String message) {
        super(message);
    }

    public BeanParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
