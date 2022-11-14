package tech.jannotti.billing.core.commons.config;

@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {
    
    public ConfigurationException(String message, Throwable cause) {
	super(message, cause);
    }    
    
}
