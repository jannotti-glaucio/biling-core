package tech.jannotti.billing.core.commons.log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class LogManagerPool {

    private ConcurrentMap<Class<?>, LogManager> logManagers = new ConcurrentHashMap<Class<?>, LogManager>();

    public LogManager getLogManager(Class<?> clazz) {

        if (logManagers.containsKey(clazz))
            return logManagers.get(clazz);
        else
            return initLogManager(clazz);
    }

    private synchronized LogManager initLogManager(Class<?> clazz) {

        if (logManagers.containsKey(clazz))
            return logManagers.get(clazz);

        LogManager logManager = LogFactory.getManager(clazz);
        logManagers.put(clazz, logManager);
        return logManager;
    }

}