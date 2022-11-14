package tech.jannotti.billing.core.commons.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.sentry.event.Event;

public class LogManager {

    private Log log = null;

    protected LogManager(Class<?> clazz) {
        log = LogFactory.getLog(clazz);
    }

    public void logINFO(String msg) {
        log.info(msg);
    }

    public void logINFO(String msg, Object... parameters) {
        log.info(String.format(msg, parameters));
    }

    public void logWARN(String msg) {
        log.warn(String.format(msg));
        SentryManager.notify(Event.Level.WARNING, msg);
    }

    public void logWARN(String msg, Object... parameters) {
        log.warn(String.format(msg, parameters));
        SentryManager.notify(Event.Level.WARNING, msg, parameters);
    }

    public void logERROR(String msg) {
        log.error(msg);
        SentryManager.notify(Event.Level.ERROR, msg);
    }

    public void logERROR(String msg, Exception e) {
        log.error(msg, e);
        SentryManager.notify(Event.Level.ERROR, msg, e);
    }

    public void logERROR(String msg, Exception e, Object... parameters) {
        log.error(String.format(msg, parameters), e);
        SentryManager.notify(Event.Level.ERROR, msg, e, parameters);
    }

    public void logERROR(String msg, Object... parameters) {
        log.error(String.format(msg, parameters));
        SentryManager.notify(Event.Level.ERROR, msg, parameters);
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    public void logDEBUG(String msg) {
        if (log.isDebugEnabled())
            log.debug(msg);
    }

    public void logDEBUG(String msg, Object... parameters) {
        if (log.isDebugEnabled())
            log.debug(String.format(msg, parameters));
    }

    public void logTRACE(String msg) {
        if (log.isTraceEnabled())
            log.trace(msg);
    }

    public void logTRACE(String msg, Object... parameters) {
        if (log.isTraceEnabled())
            log.trace(String.format(msg, parameters));
    }

}