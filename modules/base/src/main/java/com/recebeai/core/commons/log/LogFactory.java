package tech.jannotti.billing.core.commons.log;

public class LogFactory {

    public static LogManager getManager(Class<?> clazz) {
        return new LogManager(clazz);
    }

}