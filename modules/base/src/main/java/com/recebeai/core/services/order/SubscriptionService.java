package tech.jannotti.billing.core.services.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.OrderTypeEnum;
import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.repository.base.InvoiceRepository;
import tech.jannotti.billing.core.persistence.repository.base.order.OrderSubscriptionRepository;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dto.request.invoice.AddInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.subscription.AddSubscriptionServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.subscription.UpdateSubscriptionServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class SubscriptionService extends AbstractOrderService {

    @Autowired
    private OrderSubscriptionRepository subscriptionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    public Page<BaseOrderSubscription> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, SubscriptionStatusEnum[] status, String filter, Pageable pageable) {
        Page<BaseOrderSubscription> subscriptions = null;

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            subscriptions = findByExpirationDate(dealer, startDate, endDate, status, filter, pageable);
        else
            subscriptions = findByPaymentDate(dealer, startDate, endDate, status, filter, pageable);

        // Calcula os campos sumarizados dos parcelamentos
        for (BaseOrderSubscription subscription : subscriptions) {
            calculateSummaries(subscription);
        }

        return subscriptions;
    }

    public List<BaseOrderSubscription> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, SubscriptionStatusEnum[] status, String filter) {
        List<BaseOrderSubscription> subscriptions = null;

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            subscriptions = findByExpirationDate(dealer, startDate, endDate, status, filter);
        else
            subscriptions = findByPaymentDate(dealer, startDate, endDate, status, filter);

        return subscriptions;
    }

    private Page<BaseOrderSubscription> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        SubscriptionStatusEnum[] status, String filter, Pageable pageable) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate, status,
                pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter, pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            return subscriptionRepository.findByDealerAndExpirationDateBetween(dealer, startDate, endDate, pageable);
    }

    private Page<BaseOrderSubscription> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        SubscriptionStatusEnum[] status, String filter, Pageable pageable) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status,
                pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter, pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            return subscriptionRepository.findByDealerAndPaymentDateBetween(dealer, startDate, endDate, pageable);
    }

    private List<BaseOrderSubscription> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        SubscriptionStatusEnum[] status, String filter) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate,
                status);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return subscriptionRepository.findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            return subscriptionRepository.findByDealerAndExpirationDateBetween(dealer, startDate, endDate);
    }

    private List<BaseOrderSubscription> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        SubscriptionStatusEnum[] status, String filter) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return subscriptionRepository.findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            return subscriptionRepository.findByDealerAndPaymentDateBetween(dealer, startDate, endDate);
    }

    private BaseOrderSubscription get(BaseDealer dealer, String token) {
        BaseOrderSubscription subscription = subscriptionRepository.getByCustomerDealerAndToken(dealer, token);
        if (subscription == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return subscription;
    }

    public BaseOrderSubscription getAndLoad(BaseDealer dealer, String token) {
        BaseOrderSubscription subscription = get(dealer, token);

        // Carrega as faturas do parcelamento
        List<BaseInvoice> invoices = invoiceRepository.findByOrderOrderByInstalment(subscription);
        subscription.setInvoices(invoices);

        // Calcula os campos sumarizados do parcelamento
        calculateSummaries(subscription);

        return subscription;
    }

    @Transactional
    public BaseOrderSubscription add(AddSubscriptionServiceRequest request) {

        // Valida o valor da assinatura
        invoiceService.validateAmount(request.getAmount(), request.getPaymentMethod(), request.getCustomer().getDealer());
        int expirationDay = request.getStartDate().getDayOfMonth();

        BaseOrderSubscription subscription = dtoMapper.map(request, BaseOrderSubscription.class);
        subscription.setOrderType(OrderTypeEnum.SUBSCRIPTION);
        subscription.setExpirationDay(expirationDay);
        subscription.setToken(generateToken());
        subscription.setStatus(SubscriptionStatusEnum.OPEN);
        subscription.setCreationDate(DateTimeHelper.getNowDateTime());
        subscriptionRepository.save(subscription);

        // Cria a primeira fatura da assinatura
        AddInvoiceServiceRequest invoiceRequest = dtoMapper.map(request, AddInvoiceServiceRequest.class);
        invoiceRequest.setOrder(subscription);
        invoiceRequest.setReferenceDate(request.getStartDate().withDayOfMonth(1));
        invoiceRequest.setExpirationDate(request.getStartDate());
        invoiceRequest.setAmount(subscription.getAmount());
        invoiceService.add(invoiceRequest);

        return subscription;
    }

    @Transactional
    public void update(BaseDealer dealer, String token, UpdateSubscriptionServiceRequest request) {

        BaseOrderSubscription subscription = get(dealer, token);

        // Valida o valor da assinatura
        invoiceService.validateAmount(request.getAmount(), subscription.getPaymentMethod(), dealer);

        if (!subscription.getStatus().equals(SubscriptionStatusEnum.OPEN))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_SUBSCRIPTION_STATUS_TO_UPDATE);

        dtoMapper.map(request, subscription);
        subscriptionRepository.save(subscription);
    }

    private void calculateSummaries(BaseOrderSubscription subscription) {

        // TODO Ver uma forma mais otimizada de gerar esses totais

        int createdInvoices = invoiceRepository.countByOrder(subscription);
        subscription.setCreatedInvoices(createdInvoices);

        int expiredInvoices = invoiceRepository.countByOrderAndStatus(subscription, InvoiceStatusEnum.EXPIRED);
        subscription.setExpiredInvoices(expiredInvoices);

        int paidInvoices = invoiceRepository.countByOrderAndStatus(subscription, InvoiceStatusEnum.PAID);
        subscription.setPaidInvoices(paidInvoices);

        int canceledInvoices = invoiceRepository.countByOrderAndStatus(subscription, InvoiceStatusEnum.CANCELED);
        subscription.setCanceledInvoices(canceledInvoices);
    }

    @Transactional
    public void suspend(BaseDealer dealer, String token) {
        BaseOrderSubscription subscription = get(dealer, token);

        if (!subscription.getStatus().equals(SubscriptionStatusEnum.OPEN))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_SUBSCRIPTION_STATUS_TO_SUSPEND);

        subscriptionRepository.updateStatusById(subscription.getId(), SubscriptionStatusEnum.SUSPENDED);
    }

    @Transactional
    public void reopen(BaseDealer dealer, String token) {
        BaseOrderSubscription subscription = get(dealer, token);

        if (!subscription.getStatus().equals(SubscriptionStatusEnum.SUSPENDED))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_SUBSCRIPTION_STATUS_TO_REOPEN);

        subscriptionRepository.updateStatusById(subscription.getId(), SubscriptionStatusEnum.OPEN);
    }

    @Transactional
    public void cancel(BaseDealer dealer, String token) {
        BaseOrderSubscription subscription = get(dealer, token);

        if (!subscription.getStatus().equals(SubscriptionStatusEnum.OPEN))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_SUBSCRIPTION_STATUS_TO_CANCEL);

        LocalDateTime cancelationDate = DateTimeHelper.getNowDateTime();
        subscriptionRepository.updateStatusAndCancelationDateById(subscription.getId(), SubscriptionStatusEnum.CANCELED,
            cancelationDate);

        // Cancela todas as faturas nao pagas e nao canceladas
        InvoiceStatusEnum status[] = new InvoiceStatusEnum[] { InvoiceStatusEnum.PAID, InvoiceStatusEnum.CANCELED };
        List<BaseInvoice> invoices = invoiceRepository.findByOrderAndStatusNotIn(subscription, status);

        for (BaseInvoice invoice : invoices) {
            invoiceService.cancel(invoice);
        }
    }

    @Transactional
    public void finish(BaseOrderSubscription subscription, LocalDateTime finishimentDate) {
        subscriptionRepository.updateStatusAndFinishimentDateById(subscription.getId(), SubscriptionStatusEnum.FINISHED,
            finishimentDate);
    }

}
