package tech.jannotti.billing.core.services.batch;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.repository.base.InvoiceRepository;
import tech.jannotti.billing.core.persistence.repository.base.order.OrderSubscriptionRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dto.request.invoice.AddInvoiceServiceRequest;
import tech.jannotti.billing.core.services.order.SubscriptionService;

@Service
public class SubscriptionBatchService extends AbstractService {

    private LogManager logManager = LogFactory.getManager(SubscriptionBatchService.class);

    private static final int REFERENCE_DATE_DAY = 1;

    @Value("${core.subscription.invoice.daysBeforeExpirationToCreate}")
    private Integer daysBeforeExpirationToCreate;

    @Autowired
    private OrderSubscriptionRepository subscriptionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private InvoiceService invoiceService;

    public void processInvoices() {

        logManager.logINFO("Processando assinaturas com frequencia mensal");
        processMonthlySubscriptions();
        logManager.logINFO("Finalizando processamento de assinaturas com frequencia mensal");
    }

    private void processMonthlySubscriptions() {

        LocalDate startDate = DateTimeHelper.getNowDate();
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();

        LocalDate endDate = DateTimeHelper.getNowDate().plusDays(daysBeforeExpirationToCreate);
        int endDay = endDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        if (endMonth != startMonth) {
            processMonthlySubscriptions(startDay, 31, startMonth, startYear);
            processMonthlySubscriptions(1, endDay, endMonth, endYear);
        } else
            processMonthlySubscriptions(startDay, endDay, startMonth, startYear);
    }

    private void processMonthlySubscriptions(int startExpirationDay, int endExpirationDay, int expirationMonth,
        int expirationYear) {

        logManager.logINFO("Consultando assinaturas mensais que vencem entre os dias [%s] e [%s]", startExpirationDay,
            endExpirationDay);

        List<BaseOrderSubscription> subscriptions = subscriptionRepository.findByFrequencyTypeAndAndExpirationDayBetweenAndStatus(
            FrequencyTypeEnum.MONTHLY, startExpirationDay, endExpirationDay, SubscriptionStatusEnum.OPEN);

        if (CollectionUtils.isEmpty(subscriptions))
            logManager.logDEBUG("Nao existem assinaturas mensais para processar");

        for (BaseOrderSubscription subscription : subscriptions) {
            logManager.logDEBUG("Processando assinatura [token=%s]", subscription.getToken());

            LocalDate referenceDate = DateTimeHelper.getDate(expirationYear, expirationMonth, REFERENCE_DATE_DAY);
            BaseInvoice invoice = invoiceRepository.getByOrderAndReferenceDateAndStatusNot(subscription, referenceDate,
                InvoiceStatusEnum.ERROR);

            if (invoice != null) {
                logManager.logDEBUG("Assinatura jah tem Fatura nesse mes [token=%s, month=%s, year=%s]", invoice.getToken(),
                    referenceDate.getDayOfMonth(), referenceDate.getYear());
                continue;
            }

            // Calcula a proxima data de vencimento
            LocalDate expirationDate = calcExpirationDate(subscription, expirationMonth);

            if ((subscription.getEndDate() != null) && (expirationDate.compareTo(subscription.getEndDate()) <= 0)) {
                logManager.logINFO("Assinatura chegou a sua data final [endDate=%s]", subscription.getEndDate());

                // Atualiza o status da assinatura pra Finalizada
                LocalDateTime finishimentDate = DateTimeHelper.getNowDateTime();
                subscriptionService.finish(subscription, finishimentDate);
                continue;
            }

            // Cria a a proxima fatura da assinatura
            AddInvoiceServiceRequest invoiceRequest = new AddInvoiceServiceRequest();
            invoiceRequest.setOrder(subscription);
            invoiceRequest.setCustomer(subscription.getCustomer());
            invoiceRequest.setDescription(subscription.getDescription());
            invoiceRequest.setDocumentNumber(subscription.getDocumentNumber());
            invoiceRequest.setExpirationDate(expirationDate);
            invoiceRequest.setAmount(subscription.getAmount());
            invoiceRequest.setReferenceDate(expirationDate.withDayOfMonth(REFERENCE_DATE_DAY));
            invoiceRequest.setPaymentMethod(subscription.getPaymentMethod());
            BaseInvoice newInvoice = invoiceService.add(invoiceRequest);

            logManager.logINFO("Criada nova fatura [token=%s, expirationDate=%s]", newInvoice.getToken(),
                newInvoice.getExpirationDate());
        }
    }

    @Transactional
    public void processFinisheds() {

        LocalDate currentDate = DateTimeHelper.getNowDate();
        logManager.logDEBUG("Processando assinaturas finalizadas ateh [%s] para o status [%s]", currentDate,
            SubscriptionStatusEnum.FINISHED);

        List<BaseOrderSubscription> subscriptions = subscriptionRepository.findByEndDateLessThanEqualAndStatusNot(
            currentDate, SubscriptionStatusEnum.FINISHED);

        for (BaseOrderSubscription subscription : subscriptions) {
            logManager.logDEBUG("Atualizando status de Assinatura finalizada [%s]", subscription.getToken());
            LocalDateTime finishimentDate = DateTimeHelper.getNowDateTime();
            subscriptionService.finish(subscription, finishimentDate);
        }
    }

    private LocalDate calcExpirationDate(BaseOrderSubscription subscription, int expirationMonth) {
        LocalDate expirationDate = null;

        LocalDate baseDate = DateTimeHelper.getNowDate().withMonth(expirationMonth);
        try {
            expirationDate = baseDate.withDayOfMonth(subscription.getExpirationDay());

        } catch (DateTimeException e) {
            // Ultrapassou o ultimo dia de um mês
            expirationDate = baseDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        return expirationDate;
    }

}
