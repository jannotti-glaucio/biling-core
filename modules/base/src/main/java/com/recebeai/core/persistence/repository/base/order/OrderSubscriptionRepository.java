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

import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;
import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface OrderSubscriptionRepository extends AbstractRepository<BaseOrderSubscription, Long> {

    // Consultas

    // Com paginacao

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)")
    public Page<BaseOrderSubscription> findByDealerAndExpirationDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)")
    public Page<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)")
    public Page<BaseOrderSubscription> findByDealerAndPaymentDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)")
    public Page<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter,
        Pageable pageable);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public Page<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        @Param("filter") String filter,
        Pageable pageable);

    // Sem paginacao

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)")
    public List<BaseOrderSubscription> findByDealerAndExpirationDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)")
    public List<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.expirationDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderSubscription> findByDealerAndExpirationDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        @Param("filter") String filter);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)")
    public List<BaseOrderSubscription> findByDealerAndPaymentDateBetween(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)")
    public List<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndStatusIn(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("filter") String filter);

    @Query(value = "SELECT s FROM BaseOrderSubscription s WHERE s.customer.dealer=:dealer"
        + " AND EXISTS(SELECT i FROM BaseInvoice i WHERE i.order=s AND i.paymentDate BETWEEN :startDate AND :endDate)"
        + " AND s.status IN (:status)"
        + " AND (LOWER(s.customer.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(s.customer.email) LIKE LOWER(CONCAT('%',:filter,'%')) )")
    public List<BaseOrderSubscription> findByDealerAndPaymentDateBetweenAndStatusInAndFilterLike(
        @Param("dealer") BaseDealer dealer,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") SubscriptionStatusEnum[] status,
        @Param("filter") String filter);

    // Outros metodos

    public List<BaseOrderSubscription> findByFrequencyTypeAndAndExpirationDayBetweenAndStatus(FrequencyTypeEnum frequencyType,
        int startExpirationDay, int endExpirationDay, SubscriptionStatusEnum status);

    public List<BaseOrderSubscription> findByEndDateLessThanEqualAndStatusNot(LocalDate endDate, SubscriptionStatusEnum status);

    public BaseOrderSubscription getByCustomerDealerAndToken(BaseDealer dealer, String token);

    @Modifying
    @Query("UPDATE BaseOrderSubscription SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") SubscriptionStatusEnum status);

    @Modifying
    @Query("UPDATE BaseOrderSubscription SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") SubscriptionStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);

    @Modifying
    @Query("UPDATE BaseOrderSubscription SET status=:status, finishimentDate=:finishimentDate WHERE id=:id")
    public void updateStatusAndFinishimentDateById(@Param("id") long id, @Param("status") SubscriptionStatusEnum status,
        @Param("finishimentDate") LocalDateTime finishimentDate);

}
