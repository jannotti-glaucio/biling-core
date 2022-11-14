package tech.jannotti.billing.core.services;

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
import tech.jannotti.billing.core.constants.enums.AlertTypeConstants;
import tech.jannotti.billing.core.constants.enums.NotificationTypeConstants;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.repository.base.InvoiceRepository;
import tech.jannotti.billing.core.services.dto.notification.InvoiceStatusUpdateNotification;
import tech.jannotti.billing.core.services.dto.request.invoice.AddCollectionInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.request.invoice.AddInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.request.invoice.GetInvoiceFeesServiceRequest;
import tech.jannotti.billing.core.services.dto.request.invoice.UpdateInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.response.invoice.GetInvoiceFeesServiceResponse;
import tech.jannotti.billing.core.services.dto.response.invoice.SummarizeInvoicesServiceResponse;
import tech.jannotti.billing.core.services.dto.response.payment.SummarizePaymentsServiceResponse;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;
import tech.jannotti.billing.core.services.payment.BankBilletService;
import tech.jannotti.billing.core.services.payment.PaymentService;

@Service
public class InvoiceService extends AbstractService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BankBilletService bankBilletService;

    @Autowired
    private AlertingService alertingService;

    @Autowired
    private NotificationService notificationService;

    public Page<BaseInvoice> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate, InvoiceDateToFilterEnum dateToFilter,
        InvoiceStatusEnum[] status, String filter, Pageable pageable) {

        // TODO Pensar como definir o sort default, se o pageable vier sem um sort

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            return findByExpirationDate(dealer, startDate, endDate, status, filter, pageable);
        else if (dateToFilter.equals(InvoiceDateToFilterEnum.CREATION))
            return findByCreationDate(dealer, startDate, endDate, status, filter, pageable);
        else
            return findByPaymentDate(dealer, startDate, endDate, status, filter, pageable);
    }

    public Page<BaseInvoice> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate, InvoiceDateToFilterEnum dateToFilter,
        InvoiceStatusEnum[] status, Pageable pageable) {
        return find(dealer, startDate, endDate, dateToFilter, status, "", pageable);
    }

    public List<BaseInvoice> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate, InvoiceDateToFilterEnum dateToFilter,
        InvoiceStatusEnum[] status, String filter) {

        // TODO Pensar como definir o sort default, se o pageable vier sem um sort

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            return findByExpirationDate(dealer, startDate, endDate, status, filter);
        else if (dateToFilter.equals(InvoiceDateToFilterEnum.CREATION))
            return findByCreationDate(dealer, startDate, endDate, status, filter);
        else
            return findByPaymentDate(dealer, startDate, endDate, status, filter);
    }

    public List<BaseInvoice> find(BaseDealer dealer, LocalDate startDate, LocalDate endDate, InvoiceDateToFilterEnum dateToFilter,
        InvoiceStatusEnum[] status) {
        return find(dealer, startDate, endDate, dateToFilter, status, "");
    }

    public SummarizeInvoicesServiceResponse summarize(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, InvoiceStatusEnum[] status, String filter) {

        SummarizePaymentsServiceResponse paymentsSummary = paymentService.summarize(dealer, startDate, endDate, dateToFilter,
            status, filter);

        SummarizeInvoicesServiceResponse response = new SummarizeInvoicesServiceResponse(paymentsSummary.getCompanyFee(),
            paymentsSummary.getPaidAmount());
        return response;
    }

    public SummarizeInvoicesServiceResponse summarize(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, InvoiceStatusEnum[] status) {
        return summarize(dealer, startDate, endDate, dateToFilter, status, "");
    }

    private Page<BaseInvoice> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter, Pageable pageable) {

        Page<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate,
                status, pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter, pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR, pageable);

        for (BaseInvoice invoice : invoices.getContent()) {
            load(invoice);
            summarize(invoice);
        }
        return invoices;
    }

    private Page<BaseInvoice> findByCreationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter, Pageable pageable) {

        Page<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusIn(dealer, startDate, endDate,
                status, pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter, pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter, pageable);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR, pageable);

        for (BaseInvoice invoice : invoices.getContent()) {
            load(invoice);
            summarize(invoice);
        }
        return invoices;
    }

    private Page<BaseInvoice> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter, Pageable pageable) {

        Page<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status,
                pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter, pageable);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate,
                status, filter, pageable);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR, pageable);

        for (BaseInvoice invoice : invoices.getContent()) {
            summarize(invoice);
        }
        return invoices;
    }

    private List<BaseInvoice> findByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter) {

        List<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusIn(dealer, startDate, endDate,
                status);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR);

        for (BaseInvoice invoice : invoices) {
            // TODO Omitizar esse carregamento e sumarizacao para fazer direto na consulta
            load(invoice);
            summarize(invoice);
        }
        return invoices;
    }

    private List<BaseInvoice> findByCreationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter) {

        List<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusIn(dealer, startDate, endDate,
                status);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter);
        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate, status, filter);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndCreationDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR);

        for (BaseInvoice invoice : invoices) {
            // TODO Omitizar esse carregamento e sumarizacao para fazer direto na consulta
            load(invoice);
            summarize(invoice);
        }
        return invoices;
    }

    private List<BaseInvoice> findByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter) {

        List<BaseInvoice> invoices = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas e pelos status
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusIn(dealer, startDate, endDate, status);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status))
            // Busca pelas datas e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusNotAndFilterLike(dealer, startDate,
                endDate, InvoiceStatusEnum.ERROR, filter);

        else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status))
            // Busca pelas datas, pelos status e pelo filtro
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusInAndFilterLike(dealer, startDate,
                endDate,
                status, filter);
        else
            // Busca soh pelas datas
            invoices = invoiceRepository.findByCustomerDealerAndPaymentDateBetweenAndStatusNot(dealer, startDate, endDate,
                InvoiceStatusEnum.ERROR);

        for (BaseInvoice invoice : invoices) {
            summarize(invoice);
        }
        return invoices;

    }

    public Page<BaseInvoice> findExpired(BaseDealer dealer, String filter, Pageable pageable) {

        if (StringUtils.isNotBlank(filter))
            return invoiceRepository.findByCustomerDealerAndStatusAndFilterLike(dealer, InvoiceStatusEnum.EXPIRED, filter,
                pageable);
        else
            return invoiceRepository.findByCustomerDealerAndStatus(dealer, InvoiceStatusEnum.EXPIRED, pageable);
    }

    public Page<BaseInvoice> findExpiring(BaseDealer dealer, LocalDate currentDate, String filter, Pageable pageable) {

        if (StringUtils.isNotBlank(filter))
            return invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatusAndFilterLike(dealer, currentDate,
                currentDate, InvoiceStatusEnum.OPEN, filter, pageable);
        else
            return invoiceRepository.findByCustomerDealerAndExpirationDateBetweenAndStatus(dealer, currentDate, currentDate,
                InvoiceStatusEnum.OPEN, pageable);
    }

    public BaseInvoice get(BaseDealer dealer, String token) {
        BaseInvoice invoice = invoiceRepository.getByCustomerDealerAndToken(dealer, token);
        if (invoice == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return invoice;
    }

    public BaseInvoice get(String token) {
        BaseInvoice invoice = invoiceRepository.getByToken(token);
        if (invoice == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return invoice;
    }

    public BaseInvoice getAndLoadAndSummarize(BaseDealer dealer, String token) {

        BaseInvoice invoice = get(dealer, token);
        load(invoice);
        summarize(invoice);
        return invoice;
    }

    public void load(BaseInvoice invoice) {

        if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {
            BasePaymentBankBillet bankBillet = bankBilletService.getAuthorized(invoice);
            invoice.setBankBillet(bankBillet);
        }
    }

    public void summarize(BaseInvoice invoice) {

        SummarizePaymentsServiceResponse paymentAmounts = paymentService.summarize(invoice);
        invoice.setFees(paymentAmounts.getCompanyFee());
        invoice.setPaidAmount(paymentAmounts.getPaidAmount());
    }

    @Transactional(dontRollbackOn = ResultCodeServiceException.class)
    public BaseInvoice add(AddInvoiceServiceRequest request) {

        // Valida o valor da fatura
        validateAmount(request.getAmount(), request.getPaymentMethod(), request.getCustomer().getDealer());

        BaseInvoice invoice = dtoMapper.map(request, BaseInvoice.class);
        invoice.setToken(generateToken());
        invoice.setStatus(InvoiceStatusEnum.OPEN);
        invoice.setCreationDate(DateTimeHelper.getNowDateTime());
        invoiceRepository.save(invoice);

        try {
            if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {
                BasePaymentBankBillet bankBillet = bankBilletService.add(invoice, request.getPenaltyStartDate());
                invoice.setBankBillet(bankBillet);
            }

        } catch (Exception e) {
            // Se der erro na criacao do pagamento, muda o status da fatura pra erro
            invoiceRepository.updateStatusById(invoice.getId(), InvoiceStatusEnum.ERROR);
            throw e;
        }

        return invoice;
    }

    @Transactional
    public void add(List<AddCollectionInvoiceServiceRequest> requests) {
        for (AddCollectionInvoiceServiceRequest request : requests) {
            add(request);
        }
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("customer.token", 22);
    }

    @Transactional
    public BaseInvoice update(BaseDealer dealer, String token, UpdateInvoiceServiceRequest request) {

        BaseInvoice invoice = get(dealer, token);

        // Valida o valor da fatura
        validateAmount(request.getAmount(), invoice.getPaymentMethod(), dealer);

        if (!invoice.getStatus().equals(InvoiceStatusEnum.OPEN) && !invoice.getStatus().equals(InvoiceStatusEnum.EXPIRED))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_INVOICE_STATUS_TO_UPDATE);

        dtoMapper.map(request, invoice);

        if (invoice.getStatus().equals(InvoiceStatusEnum.EXPIRED))
            invoice.setStatus(InvoiceStatusEnum.OPEN);
        invoiceRepository.save(invoice);

        if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET))
            bankBilletService.update(invoice, request.getPenaltyStartDate());

        return invoice;
    }

    public void validateAmount(int amount, PaymentMethodEnum paymentMethod, BaseDealer dealer) {

        int minimumAmount = dealer.getCompany().getMinimumInvoiceAmount();
        if (amount < minimumAmount)
            throw new ResultCodeServiceException(CODE_INVOICE_AMOUNT_BELOW_MINIMUM);

        int maximumAmount = dealer.getCompany().getMaximumInvoiceAmount();
        if (amount > maximumAmount)
            throw new ResultCodeServiceException(CODE_INVOICE_AMOUNT_ABOVE_MAXIMUM);

        Integer companyFee = null;
        if (paymentMethod.equals(PaymentMethodEnum.BANK_BILLET))
            companyFee = dealer.getBillingPlan().getPaidBankBilletFee();
        else
            companyFee = 0;

        if ((amount - companyFee) <= 0)
            throw new ResultCodeServiceException(CODE_INVOICE_NET_AMOUNT_BELOW_MINIMUM);
    }

    public byte[] printBankBillet(BaseDealer dealer, String token) {
        BaseInvoice invoice = get(dealer, token);
        return printBankBillet(invoice);
    }

    public byte[] printBankBillet(String token) {
        BaseInvoice invoice = get(token);
        return printBankBillet(invoice);
    }

    public byte[] printBankBillet(BaseInvoice invoice) {

        if (!invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVOICE_BANK_BILLET_PAYMENT_METHOD_REQUIRED);

        return bankBilletService.print(invoice);
    }

    public void sendAlert(BaseDealer dealer, String token, MediaTypeEnum mediaType) {

        BaseInvoice invoice = get(dealer, token);
        if (mediaType.equals(MediaTypeEnum.EMAIL))
            alertingService.dispatchInvoiceAlert(invoice, MediaTypeEnum.EMAIL, AlertTypeConstants.INVOICE_SEND);
    }

    public GetInvoiceFeesServiceResponse getFees(BaseDealer dealer, int amount, PaymentMethodEnum paymentMethod) {
        Integer fees = null;
        if (paymentMethod.equals(PaymentMethodEnum.BANK_BILLET))
            fees = dealer.getBillingPlan().getPaidBankBilletFee();
        else
            fees = 0;

        int netAmount = amount - fees;
        return new GetInvoiceFeesServiceResponse(fees, netAmount);
    }

    public GetInvoiceFeesServiceResponse getFees(BaseDealer dealer, GetInvoiceFeesServiceRequest request) {
        return getFees(dealer, request.getAmount(), request.getPaymentMethod());
    }

    @Transactional
    public void pay(BaseInvoice invoice, LocalDate paymentDate) {
        invoiceRepository.updateStatusAndPaymentDateById(invoice.getId(), InvoiceStatusEnum.PAID, paymentDate);

        // Notifica sobre o pagamento da fatura
        sendUpdateStatusNotification(invoice, InvoiceStatusEnum.PAID);
    }

    @Transactional
    public void cancel(BaseDealer dealer, String token) {
        BaseInvoice invoice = get(dealer, token);
        cancel(invoice);
    }

    @Transactional
    public void cancel(BaseInvoice invoice) {

        if (invoice.getStatus().equals(InvoiceStatusEnum.PAID) || invoice.getStatus().equals(InvoiceStatusEnum.CANCELED))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_INVOICE_STATUS_TO_CANCEL);

        bankBilletService.cancel(invoice);

        LocalDateTime cancelationDate = DateTimeHelper.getNowDateTime();
        invoiceRepository.updateStatusAndCancelationDateById(invoice.getId(), InvoiceStatusEnum.CANCELED, cancelationDate);

        // Notifica sobre o cancelamento da fatura
        sendUpdateStatusNotification(invoice, InvoiceStatusEnum.CANCELED);
    }

    private void sendUpdateStatusNotification(BaseInvoice invoice, InvoiceStatusEnum status) {

        if (StringUtils.isNotBlank(invoice.getCallbackUrl())) {
            InvoiceStatusUpdateNotification notification = new InvoiceStatusUpdateNotification(invoice.getToken(),
                invoice.getDocumentNumber(), DateTimeHelper.getNowDate(), status);

            notificationService.dispatch(invoice, NotificationTypeConstants.INVOICE_STATUS_UPDATE.getCode(), notification);
        }
    }

}
