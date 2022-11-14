package tech.jannotti.billing.core.commons.exception;

@SuppressWarnings("serial")
public class EnumNotFoundException extends RuntimeException {

    public EnumNotFoundException(String label) {
        super("Nao localizado enum com label [" + label + "]");
    }

}
