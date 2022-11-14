package tech.jannotti.billing.core.services.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.constants.enums.MarketStatamentTypeConstants;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dealer.DealerMarketService;
import tech.jannotti.billing.core.services.dto.response.payment.SummarizePaymentsServiceResponse;
import tech.jannotti.billing.core.services.transfer.AbstractTransferService;

@Service
public class PaymentService extends AbstractPaymentService {

    private LogManager logManager = LogFactory.getManager(AbstractTransferService.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private DealerMarketService dealerMarketService;

    public BasePayment getAuthorized(BaseInvoice invoice) {
        PaymentStatusEnum[] status = new PaymentStatusEnum[] { PaymentStatusEnum.AUTHORIZED };
        return paymentRepository.getByInvoiceAndStatusIn(invoice, status);
    }

    public SummarizePaymentsServiceResponse summarize(BaseInvoice invoice) {

        Integer companyFee = paymentRepository.sumCompanyFeeByInvoiceAndStatus(invoice, PaymentStatusEnum.CAPTURED);
        Integer paidAmount = paymentRepository.sumPaidAmountByInvoiceAndStatus(invoice, PaymentStatusEnum.CAPTURED);

        SummarizePaymentsServiceResponse response = new SummarizePaymentsServiceResponse(companyFee, paidAmount);
        return response;
    }

    public SummarizePaymentsServiceResponse summarize(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceDateToFilterEnum dateToFilter, InvoiceStatusEnum[] status, String filter) {

        if (dateToFilter == null)
            dateToFilter = InvoiceDateToFilterEnum.EXPIRATION;

        if (dateToFilter.equals(InvoiceDateToFilterEnum.EXPIRATION))
            return summarizeByExpirationDate(dealer, startDate, endDate, status, filter);
        else
            return summarizeByPaymentDate(dealer, startDate, endDate, status, filter);
    }

    private SummarizePaymentsServiceResponse summarizeByExpirationDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter) {

        Integer companyFee = null;
        Integer paidAmount = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status)) {
            // Busca pelas datas e pelos status
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusIn(
                    dealer, startDate, endDate, status);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusIn(dealer,
                    startDate, endDate, status);

        } else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status)) {
            // Busca pelas datas e pelo filtro
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
                    dealer, startDate, endDate, InvoiceStatusEnum.ERROR, filter);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
                    dealer, startDate, endDate, InvoiceStatusEnum.ERROR, filter);

        } else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status)) {
            // Busca pelas datas, pelos status e pelo filtro
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
                    dealer, startDate, endDate, status, filter);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
                    dealer, startDate, endDate, status, filter);

        } else {
            // Busca soh pelas datas
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNot(dealer,
                    startDate, endDate, InvoiceStatusEnum.ERROR);
            paidAmount = paymentRepository.sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNot(
                dealer, startDate, endDate, InvoiceStatusEnum.ERROR);
        }

        SummarizePaymentsServiceResponse response = new SummarizePaymentsServiceResponse(companyFee, paidAmount);
        return response;
    }

    private SummarizePaymentsServiceResponse summarizeByPaymentDate(BaseDealer dealer, LocalDate startDate, LocalDate endDate,
        InvoiceStatusEnum[] status, String filter) {

        Integer companyFee = null;
        Integer paidAmount = null;

        if (StringUtils.isBlank(filter) && ArrayUtils.isNotEmpty(status)) {
            // Busca pelas datas e pelos status
            companyFee = paymentRepository.sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusIn(
                dealer, startDate, endDate, status);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusIn(dealer,
                    startDate, endDate, status);

        } else if (StringUtils.isNotBlank(filter) && ArrayUtils.isEmpty(status)) {
            // Busca pelas datas e pelo filtro
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
                    dealer, startDate, endDate, InvoiceStatusEnum.ERROR, filter);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(dealer,
                    startDate, endDate, InvoiceStatusEnum.ERROR, filter);

        } else if (StringUtils.isNotBlank(filter) && ArrayUtils.isNotEmpty(status)) {
            // Busca pelas datas, pelos status e pelo filtro
            companyFee = paymentRepository
                .sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(dealer,
                    startDate, endDate, status, filter);
            paidAmount = paymentRepository
                .sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
                    dealer, startDate, endDate, status, filter);

        } else {
            // Busca soh pelas datas
            companyFee = paymentRepository.sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNot(
                dealer, startDate, endDate, InvoiceStatusEnum.ERROR);
            paidAmount = paymentRepository.sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNot(
                dealer, startDate, endDate, InvoiceStatusEnum.ERROR);
        }

        SummarizePaymentsServiceResponse response = new SummarizePaymentsServiceResponse(companyFee, paidAmount);
        return response;
    }

    @Transactional
    public void authorize(BasePayment payment) {
        paymentRepository.updateStatusById(payment.getId(), PaymentStatusEnum.AUTHORIZED);
    }

    @Transactional
    public void deny(BasePayment payment) {
        paymentRepository.updateStatusById(payment.getId(), PaymentStatusEnum.DENIED);
    }

    @Transactional
    public void capture(BasePayment payment, LocalDate paymentDate, int paidAmount, int paymentCost, LocalDate releaseDate,
        int releasedAmount) {

        BaseDealer dealer = payment.getInvoice().getCustomer().getDealer();
        int companyFee = dealer.getBillingPlan().getPaidBankBilletFee();

        // Atualiza o pagamento
        payment.setStatus(PaymentStatusEnum.CAPTURED);
        payment.setPaymentDate(paymentDate);
        payment.setPaidAmount(paidAmount);
        payment.setPaymentCost(paymentCost);
        payment.setReleaseDate(releaseDate);
        payment.setReleasedAmount(releasedAmount);
        payment.setCompanyFee(companyFee);
        paymentRepository.save(payment);

        // Libera o credito do valor pago na conta do Dealer
        dealerMarketService.addStatement(dealer, MarketStatamentTypeConstants.INVOICE_PAYMENT.getCode(), paymentDate,
            payment.getNetAmount(), payment);

        // Muda a fatura para paga, se o valor pago for igual ou maior que da fatura
        if (paidAmount >= payment.getInvoice().getAmount())
            invoiceService.pay(payment.getInvoice(), paymentDate);
        else
            logManager.logWARN("Pagamento pago com valor abaixo da fatura [token=%s, valorFatura=%s, valorPago=%]",
                payment.getToken(), payment.getInvoice().getAmount(), paidAmount);
        // TODO Pensar no que fazer nos cenarios que valor pago for menor que a fatura
    }

    @Transactional
    public void requestCancelation(BasePayment payment, LocalDateTime requestDate) {
        paymentRepository.updateStatusAndCancelationRequestDateById(payment.getId(), PaymentStatusEnum.CANCELATION_REQUESTED,
            requestDate);
    }

    @Transactional
    public void cancel(BasePayment payment, LocalDateTime cancelationDate) {
        paymentRepository.updateStatusAndCancelationDateById(payment.getId(), PaymentStatusEnum.CANCELED, cancelationDate);
        // TODO enviar notificaçao de boleto cancelado, a fatura deverá ficar no status 'expired'
    }

    @Transactional
    public void purge(BasePayment payment, LocalDateTime purgeDate) {

        paymentRepository.updateStatusAndCancelationDateById(payment.getId(), PaymentStatusEnum.CANCELED, purgeDate);
        // TODO enviar notificaçao de boleto cancelado, a fatura deverá ficar no status 'expired'
    }

}
