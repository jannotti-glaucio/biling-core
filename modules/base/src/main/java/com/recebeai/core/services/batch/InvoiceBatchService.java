package tech.jannotti.billing.core.services.batch;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.enums.AlertTypeConstants;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseAlertType;
import tech.jannotti.billing.core.persistence.repository.base.InvoiceRepository;
import tech.jannotti.billing.core.persistence.repository.base.alert.AlertTypeRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.AlertingService;

@Service
public class InvoiceBatchService extends AbstractService {

    private LogManager logManager = LogFactory.getManager(InvoiceBatchService.class);

    @Value("${core.invoice.alerting.daysBeforeExpirationToAlert}")
    private Integer daysBeforeExpirationToAlert;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private AlertingService alertingService;

    @Autowired
    private AlertTypeRepository alertTypeRepository;

    @Transactional
    public void processExpireds() {

        LocalDate expirationDate = DateTimeHelper.getNowDate();
        logManager.logDEBUG("Processando faturas vencidas antes de [%s] para o status [%s]", expirationDate,
            InvoiceStatusEnum.EXPIRED);

        List<BaseInvoice> invoices = invoiceRepository.findByStatusAndExpirationDateLessThan(InvoiceStatusEnum.OPEN,
            expirationDate);

        for (BaseInvoice invoice : invoices) {
            logManager.logDEBUG("Atualizando status de fatura vencida [%s]", invoice.getToken());
            invoiceRepository.updateStatusById(invoice.getId(), InvoiceStatusEnum.EXPIRED);
        }
    }

    public void processAlerts() {

        // Envia alerta de parcelas vencendo
        BaseAlertType alertType = alertTypeRepository.getByCode(AlertTypeConstants.COLLECTION_INVOICE_EXPIRING.getCode());

        LocalDate expirationDate = LocalDate.now().plusDays(daysBeforeExpirationToAlert);
        logManager.logINFO("Verificando se existem parcelas vencendo ateh [%s] para enviar Alerta", expirationDate);

        List<BaseInvoice> invoices = invoiceRepository
            .findByStatusAndExpirationDateLessThanEqualsAndCollectionAndAlertNotExistsByType(InvoiceStatusEnum.OPEN,
                expirationDate, alertType);

        for (BaseInvoice invoice : invoices) {

            if (StringUtils.isNotBlank(invoice.getCustomer().getEmail())) {
                logManager.logDEBUG("Enviando Alerta via email de parcela vencendo para Fatura [%s]", invoice.getToken());
                alertingService.dispatchInvoiceAlert(invoice, MediaTypeEnum.EMAIL, alertType);
            }
        }
    }

}
