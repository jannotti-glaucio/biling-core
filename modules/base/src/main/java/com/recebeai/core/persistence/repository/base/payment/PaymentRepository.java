package tech.jannotti.billing.core.persistence.repository.base.payment;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

@Repository
public interface PaymentRepository extends AbstractPaymentRepository<BasePayment> {

    public BasePayment getByInvoiceAndStatusIn(BaseInvoice invoice, PaymentStatusEnum[] status);

    // Agregacoes

    // Valor pago

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice=:invoice and status=:status")
    public Integer sumPaidAmountByInvoiceAndStatus(@Param("invoice") BaseInvoice invoice,
        @Param("status") PaymentStatusEnum status);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate) AND invoice.status != :status")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status != :status"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate) AND invoice.status != :status")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status != :status"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(paidAmount) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumPaidAmountByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    // Taxas

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice=:invoice and status=:status")
    public Integer sumCompanyFeeByInvoiceAndStatus(@Param("invoice") BaseInvoice invoice,
        @Param("status") PaymentStatusEnum status);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate) AND invoice.status != :status")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status != :status"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoiceExpirationDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate) AND invoice.status != :status")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status != :status"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusNotAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT SUM(companyFee) FROM BasePayment WHERE invoice.customer.dealer=:dealer"
        + " AND (invoice.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND invoice.status IN (:status)"
        + " AND (LOWER(invoice.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(invoice.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Integer sumCompanyFeeByInvoiceCustomerDealerAndInvoicePaymentDateBetweenAndInvoiceStatusInAndInvoiceFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

}
