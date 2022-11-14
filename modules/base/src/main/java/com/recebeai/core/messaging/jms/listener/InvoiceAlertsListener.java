package tech.jannotti.billing.core.messaging.jms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.messaging.jms.MessagingConstants;
import tech.jannotti.billing.core.services.AlertingService;

@Component
public class InvoiceAlertsListener {

    private static final LogManager logManager = LogFactory.getManager(InvoiceAlertsListener.class);

    @Autowired
    private AlertingService alertingService;

    @JmsListener(destination = MessagingConstants.INVOICE_ALERTS_QUEUE)
    public void process(Long invoiceAlertId) {

        try {
            alertingService.sendInvoiceAlert(invoiceAlertId);
        } catch (Exception e) {
            logManager.logERROR("Erro enviando Aviso de Fatura [id=%s]", e, invoiceAlertId);
        }
    }

}
