package tech.jannotti.billing.core.persistence.repository.base.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface OrderCollectionRepository extends AbstractRepository<BaseOrderCollection, Long> {

    // Consultas

    // Com paginacao

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)")
    public Page<BaseOrderCollection> findByDealerAndExpirationDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)")
    public Page<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)")
    public Page<BaseOrderCollection> findByDealerAndPaymentDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)")
    public Page<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    // Sem paginacao

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)")
    public List<BaseOrderCollection> findByDealerAndExpirationDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)")
    public List<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderCollection> findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        @Param("filter") String filter);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)")
    public List<BaseOrderCollection> findByDealerAndPaymentDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)")
    public List<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter);

    @Query(value = "SELECT c FROM BaseOrderCollection c WHERE c.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=c AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND c.status IN (:status)"
        + " AND (LOWER(c.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderCollection> findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") CollectionStatusEnum[] status,
        @Param("filter") String filter);

    // Outros metodos

    public BaseOrderCollection getByCustomerDealerAndToken(BaseDealer dealer, String token);

    @Modifying
    @Query("UPDATE BaseOrderCollection SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") CollectionStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);

}
