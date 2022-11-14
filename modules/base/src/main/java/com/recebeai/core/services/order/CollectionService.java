package tech.jannotti.billing.core.services.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.CollectionAmountTypeEnum;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.commons.util.NumberHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.OrderTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.repository.base.InvoiceRepository;
import tech.jannotti.billing.core.persistence.repository.base.order.OrderCollectionRepository;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dto.model.OrderInstalmentServiceDTO;
import tech.jannotti.billing.core.services.dto.request.invoice.AddCollectionInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.collection.CollectionServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.collection.GetCollectionInstalmentsServiceRequest;
import tech.jannotti.billing.core.services.dto.response.invoice.GetInvoiceFeesServiceResponse;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class CollectionService extends AbstractOrderService {

    @Autowired
    private OrderCollectionRepository collectionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    public Page<BaseOrderCollection> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, CollectionStatusEnum[] status, String filter, Pageable pageable) {
        Page<BaseOrderCollection> collections = null;

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            collections = findByExpirationDate(dealer, startDate, endDate, status, filter, pageable);
        else
            collections = findByPaymentDate(dealer, startDate, endDate, status, filter, pageable);

        // Calcula os campos sumarizados dos parcelamentos
        for (BaseOrderCollection collection : collections) {
            calculateSummaries(collection, true);
        }

        return collections;
    }

    public List<BaseOrderCollection> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, CollectionStatusEnum[] status, String filter) {
        List<BaseOrderCollection> collections = null;

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            collections = findByExpirationDate(dealer, startDate, endDate, status, filter);
        else
            collections = findByPaymentDate(dealer, startDate, endDate, status, filter);

        // Calcula os campos sumarizados dos parcelamentos
        for (BaseOrderCollection collection : collections) {
            calculateSummaries(collection, false);
        }

        return collections;
    }

    private Page<BaseOrderCollection> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        CollectionStatusEnum[] status, String filter, Pageable pageable) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return collectionRepository.findByDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate, status,
                pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return collectionRepository.findByDealerAndExpirationDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter, pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return collectionRepository.findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            return collectionRepository.findByDealerAndExpirationDateBetween(dealer, startDate, endDate, pageable);
    }

    private Page<BaseOrderCollection> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        CollectionStatusEnum[] status, String filter, Pageable pageable) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return collectionRepository.findByDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status,
                pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return collectionRepository.findByDealerAndPaymentDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter, pageable);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return collectionRepository.findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            return collectionRepository.findByDealerAndPaymentDateBetween(dealer, startDate, endDate, pageable);
    }

    private List<BaseOrderCollection> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        CollectionStatusEnum[] status, String filter) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return collectionRepository.findByDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate, status);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return collectionRepository.findByDealerAndExpirationDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return collectionRepository.findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            return collectionRepository.findByDealerAndExpirationDateBetween(dealer, startDate, endDate);
    }

    private List<BaseOrderCollection> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        CollectionStatusEnum[] status, String filter) {

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            return collectionRepository.findByDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            return collectionRepository.findByDealerAndPaymentDateBetweenAndFilterLike(dealer, startDate, endDate,
                filter);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            return collectionRepository.findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            return collectionRepository.findByDealerAndPaymentDateBetween(dealer, startDate, endDate);
    }

    private BaseOrderCollection get(BaseDealer dealer, String token) {
        BaseOrderCollection collection = collectionRepository.getByCustomerDealerAndToken(dealer, token);
        if (collection == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return collection;
    }

    public BaseOrderCollection getAndLoad(BaseDealer dealer, String token) {
        BaseOrderCollection collection = get(dealer, token);

        // Carrega as faturas do parcelamento
        List<BaseInvoice> invoices = invoiceRepository.findByOrderOrderByInstalment(collection);
        collection.setInvoices(invoices);

        // Calcula os campos sumarizados do parcelamento
        calculateSummaries(collection, true);

        return collection;
    }

    @Transactional
    public BaseOrderCollection add(CollectionServiceRequest request) {

        BaseOrderCollection collection = dtoMapper.map(request, BaseOrderCollection.class);
        collection.setOrderType(OrderTypeEnum.COLLECTION);
        collection.setToken(generateToken());
        collection.setStatus(CollectionStatusEnum.OPEN);
        collection.setCreationDate(DateTimeHelper.getNowDateTime());
        collectionRepository.save(collection);

        List<OrderInstalmentServiceDTO> collectionInstalments = calculateInstalments(collection.getCustomer().getDealer(),
            collection.getInstalments(), request.getExpirationDate(), request.getAmount(), request.getAmountType(),
            request.getPaymentMethod());

        // Cria as faturas do parcelamento
        List<AddCollectionInvoiceServiceRequest> invoiceRequets = new ArrayList<AddCollectionInvoiceServiceRequest>();
        for (OrderInstalmentServiceDTO collectionInstalment : collectionInstalments) {

            AddCollectionInvoiceServiceRequest invoiceRequest = dtoMapper.map(request, AddCollectionInvoiceServiceRequest.class);
            invoiceRequest.setOrder(collection);
            invoiceRequest.setExpirationDate(collectionInstalment.getExpirationDate());
            invoiceRequest.setAmount(collectionInstalment.getAmount());
            invoiceRequest.setInstalment(collectionInstalment.getInstalment());

            invoiceRequets.add(invoiceRequest);
        }
        invoiceService.add(invoiceRequets);

        return collection;
    }

    private List<OrderInstalmentServiceDTO> calculateInstalments(BaseDealer dealer, int instalments,
        LocalDate expirationDate, int amount, CollectionAmountTypeEnum amountType, PaymentMethodEnum paymentMethod) {

        List<OrderInstalmentServiceDTO> orderInstalments = new ArrayList<OrderInstalmentServiceDTO>();

        LocalDate lastExpirationDate = null;
        for (int i = 0; i < instalments; i++) {
            int instalment = i + 1;

            // Calcula o vencimento da parcela
            if (instalment == 1)
                // Na primeira fatura, usa a data de vencimento da requisicao
                lastExpirationDate = expirationDate;
            else
                // Nas outras faturas, incrementa o ultimo vencimento em 1 mês
                lastExpirationDate = lastExpirationDate.plus(1, ChronoUnit.MONTHS);

            // Calcula o valor da parcela
            Integer installmentAmount = null;
            if (amountType.equals(CollectionAmountTypeEnum.INVOICE))
                installmentAmount = amount;
            else
                installmentAmount = calculateAmount(amount, instalment, instalments);

            // Valida o valor da parcela
            invoiceService.validateAmount(amount, paymentMethod, dealer);

            GetInvoiceFeesServiceResponse invoiceFess = invoiceService.getFees(dealer, installmentAmount, paymentMethod);

            OrderInstalmentServiceDTO orderInstalment = new OrderInstalmentServiceDTO();
            orderInstalment.setInstalment(instalment);
            orderInstalment.setExpirationDate(lastExpirationDate);
            orderInstalment.setAmount(installmentAmount);
            orderInstalment.setFees(invoiceFess.getFees());
            orderInstalment.setNetAmount(invoiceFess.getNetAmount());

            orderInstalments.add(orderInstalment);
        }

        return orderInstalments;
    }

    int calculateAmount(int amount, int currentInstallment, int instalments) {

        BigDecimal amountDecimal = new BigDecimal(amount);
        BigDecimal instalmentsDecimal = new BigDecimal(instalments);
        BigDecimal amountDivision = amountDecimal.divide(instalmentsDecimal, RoundingMode.HALF_DOWN);

        if (currentInstallment == 1) {
            BigDecimal amountRemainder = amountDecimal.remainder(instalmentsDecimal);
            return amountDivision.add(amountRemainder).intValue();

        } else {
            return amountDivision.intValue();
        }
    }

    public List<OrderInstalmentServiceDTO> getInstalments(BaseDealer dealer,
        GetCollectionInstalmentsServiceRequest request) {

        List<OrderInstalmentServiceDTO> collectionInstalments = calculateInstalments(dealer, request.getInstalments(),
            request.getExpirationDate(), request.getAmount(), request.getAmountType(), request.getPaymentMethod());
        return collectionInstalments;
    }

    private void calculateSummaries(BaseOrderCollection collection, boolean countInvoices) {

        // TODO Ver uma forma mais otimizada de gerar esses totais

        Long totalAmount = invoiceRepository.sumAmountByOrderAndStatusNot(collection, InvoiceStatusEnum.CANCELED);
        collection.setTotalAmount(NumberHelper.fromLongToInteger(totalAmount));

        Long paidAmount = invoiceRepository.sumAmountByOrderAndStatus(collection, InvoiceStatusEnum.PAID);
        collection.setPaidAmount(NumberHelper.fromLongToInteger(paidAmount));

        int pendingAmount = collection.getTotalAmount() - collection.getPaidAmount();
        collection.setPendingAmount(pendingAmount);

        if (countInvoices) {
            int expiredInvoices = invoiceRepository.countByOrderAndStatus(collection, InvoiceStatusEnum.EXPIRED);
            collection.setExpiredInvoices(expiredInvoices);

            int paidInvoices = invoiceRepository.countByOrderAndStatus(collection, InvoiceStatusEnum.PAID);
            collection.setPaidInvoices(paidInvoices);

            int canceledInvoices = invoiceRepository.countByOrderAndStatus(collection, InvoiceStatusEnum.CANCELED);
            collection.setCanceledInvoices(canceledInvoices);
        }
    }

    @Transactional
    public void cancel(BaseDealer dealer, String token) {
        BaseOrderCollection collection = get(dealer, token);

        if (!collection.getStatus().equals(CollectionStatusEnum.OPEN))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_COLLECTION_STATUS_TO_CANCEL);

        LocalDateTime cancelationDate = DateTimeHelper.getNowDateTime();
        collectionRepository.updateStatusAndCancelationDateById(collection.getId(), CollectionStatusEnum.CANCELED,
            cancelationDate);

        // Cancela todas as faturas nao pagas e nao canceladas
        InvoiceStatusEnum status[] = new InvoiceStatusEnum[] { InvoiceStatusEnum.PAID, InvoiceStatusEnum.CANCELED };
        List<BaseInvoice> invoices = invoiceRepository.findByOrderAndStatusNotIn(collection, status);

        for (BaseInvoice invoice : invoices) {
            invoiceService.cancel(invoice);
        }
    }

}
