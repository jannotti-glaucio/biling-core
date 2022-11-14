package tech.jannotti.billing.core.commons.exception;

public interface ResultCodeException {

    public String getResultCodeKey();

    public String[] getParameters();

}
