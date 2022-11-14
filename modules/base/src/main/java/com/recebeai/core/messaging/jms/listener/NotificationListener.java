package tech.jannotti.billing.core.messaging.jms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.messaging.jms.MessagingConstants;
import tech.jannotti.billing.core.services.NotificationService;

@Component
public class NotificationListener {

    private static final LogManager logManager = LogFactory.getManager(NotificationListener.class);

    @Autowired
    private NotificationService notificationService;

    @JmsListener(destination = MessagingConstants.NOTIFICATIONS_QUEUE)
    public void process(Long notificationId) {

        try {
            notificationService.process(notificationId);
        } catch (Exception e) {
            logManager.logERROR("Erro procesando notificacao [id=%s]", e, notificationId);
        }
    }

}
