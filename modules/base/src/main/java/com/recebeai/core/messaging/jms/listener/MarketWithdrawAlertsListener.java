package tech.jannotti.billing.core.messaging.jms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.messaging.jms.MessagingConstants;
import tech.jannotti.billing.core.services.AlertingService;

@Component
public class MarketWithdrawAlertsListener {

    private static final LogManager logManager = LogFactory.getManager(MarketWithdrawAlertsListener.class);

    @Autowired
    private AlertingService alertingService;

    @JmsListener(destination = MessagingConstants.MARKET_WITHDRAW_ALERTS_QUEUE)
    public void process(Long withdrawAlertId) {

        try {
            alertingService.sendMarketWithdrawAlert(withdrawAlertId);
        } catch (Exception e) {
            logManager.logERROR("Erro enviando Aviso de Saque [id=%s]", e, withdrawAlertId);
        }
    }

}
