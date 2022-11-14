package tech.jannotti.billing.core.commons.log;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;

@Component
@SuppressWarnings("static-access")
public class SentryManager {

    private static final LogManager logManager = LogFactory.getManager(SentryManager.class);

    private static Boolean enabled;

    private static String dsn;

    private static String environment;

    private static boolean initialized = false;

    @PostConstruct
    public void init() {
        if (!initialized && BooleanUtils.isTrue(enabled))
            initSentry();
    }

    @Value("${sentry.enabled}")
    public void setEnbled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Value("${sentry.dsn}")
    public void setDsn(String dsn) {
        this.dsn = dsn;
    }

    @Value("${sentry.environment}")
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    private static synchronized void initSentry() {
        if (!initialized) {
            logManager.logINFO("Inicializando servico Sentry");

            Sentry.init(dsn);
            initialized = true;

            logManager.logINFO("Servico Sentry inicializado com sucesso");
        }
    }

    public static void notify(Event.Level level, String msg, Exception e, Object... parameters) {

        if (BooleanUtils.isNotTrue(enabled))
            return;

        if (!initialized)
            initSentry();

        EventBuilder eventBuilder = new EventBuilder()
            .withMessage(String.format(msg, parameters))
            .withLevel(level)
            .withEnvironment(environment);

        if (e != null)
            eventBuilder.withSentryInterface(new ExceptionInterface(e));

        Sentry.capture(eventBuilder);
    }

    public static void notify(Event.Level level, String msg, Object... parameters) {
        notify(level, msg, null, parameters);
    }

}
