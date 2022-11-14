package tech.jannotti.billing.core.persistence.repository.base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseAlertType;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrder;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface InvoiceRepository extends AbstractRepository<BaseInvoice, Long> {

    // Pela data de vencimento

    // Com paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate) AND i.status != :status")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    // Sem paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate) AND i.status != :status")
    public List<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public List<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    // Pela data de criação

    // Com paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate) AND i.status != :status")
    public Page<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public Page<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    // Sem paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate) AND i.status != :status")
    public List<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public List<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (date(i.creationDate) BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndCreationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    // Pela data de pagamento

    // Com paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate) AND i.status != :status")
    public Page<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public Page<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    // Sem paginacao

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate) AND i.status != :status")
    public List<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusNot(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)")
    public List<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status != :status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusNotAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND i.status IN (:status)"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseInvoice> findByCustomerDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum[] status,
        @Param("filter") String filter);

    // Outras consultas

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status=:status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatusAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND (i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND i.status=:status")
    public Page<BaseInvoice> findByCustomerDealerAndExpirationDateBetweenAndStatus(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") InvoiceStatusEnum status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer AND i.status=:status")
    public Page<BaseInvoice> findByCustomerDealerAndStatus(
        @Param("dealer") BaseDealer dealer,
        @Param("status") InvoiceStatusEnum status,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.customer.dealer=:dealer"
        + " AND i.status=:status"
        + " AND (LOWER(i.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(i.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseInvoice> findByCustomerDealerAndStatusAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("status") InvoiceStatusEnum status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT i FROM BaseInvoice i WHERE i.status=:status AND i.expirationDate <= :expirationDate"
        + " AND EXISTS (SELECT c FROM BaseOrderCollection c WHERE i.order = c)"
        + " AND NOT EXISTS (SELECT a FROM BaseInvoiceAlert a WHERE a.invoice = i and a.alertType=:alertType)")
    public List<BaseInvoice> findByStatusAndExpirationDateLessThanEqualsAndCollectionAndAlertNotExistsByType(
        @Param("status") InvoiceStatusEnum status, @Param("expirationDate") LocalDate expirationDate,
        @Param("alertType") BaseAlertType alertType);

    public List<BaseInvoice> findByOrderOrderByInstalment(BaseOrder order);

    public List<BaseInvoice> findByOrderAndStatusNotIn(BaseOrder order, InvoiceStatusEnum[] status);

    public List<BaseInvoice> findByStatusAndExpirationDateLessThan(InvoiceStatusEnum status, LocalDate expirationDate);

    public BaseInvoice getByToken(String token);

    public BaseInvoice getByCustomerDealerAndToken(BaseDealer dealer, String token);

    public BaseInvoice getByOrderAndReferenceDateAndStatusNot(BaseOrder order, LocalDate referenceDate, InvoiceStatusEnum status);

    // Agregacoes

    @Query(value = "SELECT SUM(amount) FROM BaseInvoice i WHERE i.order=:order AND i.status=:status")
    public Long sumAmountByOrderAndStatus(@Param("order") BaseOrder order, @Param("status") InvoiceStatusEnum status);

    @Query(value = "SELECT SUM(amount) FROM BaseInvoice i WHERE i.order=:order AND i.status!=:status")
    public Long sumAmountByOrderAndStatusNot(@Param("order") BaseOrder order, @Param("status") InvoiceStatusEnum status);

    public int countByOrder(BaseOrder order);

    public int countByOrderAndStatus(BaseOrder order, InvoiceStatusEnum status);

    // Atualizacoes

    @Modifying
    @Query("UPDATE BaseInvoice SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") InvoiceStatusEnum status);

    @Modifying
    @Query("UPDATE BaseInvoice SET status=:status, paymentDate=:paymentDate WHERE id=:id")
    public void updateStatusAndPaymentDateById(@Param("id") long id, @Param("status") InvoiceStatusEnum status,
        @Param("paymentDate") LocalDate paymentDate);

    @Modifying
    @Query("UPDATE BaseInvoice SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") InvoiceStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);

}