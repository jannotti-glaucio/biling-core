package tech.jannotti.billing.core.persistence.repository.base.market;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface MarketWithdrawRepository extends AbstractRepository<BaseMarketWithdraw, Long> {

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount=:marketAccount AND "
        + "(DATE(w.requestDate) BETWEEN :startDate AND :endDate)")
    public Page<BaseMarketWithdraw> findByMarketAccountAndRequestDateBetween(
        @Param("marketAccount") BaseMarketAccount marketAccount,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts) AND "
        + "(DATE(w.requestDate) BETWEEN :startDate AND :endDate) AND w.status IN (:status)")
    public Page<BaseMarketWithdraw> findByMarketAccountInAndRequestDateBetweenAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") MarketWithdrawStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts) AND "
        + "(DATE(w.requestDate) BETWEEN :startDate AND :endDate) AND w.status IN (:status)")
    public List<BaseMarketWithdraw> findByMarketAccountInAndRequestDateBetweenAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") MarketWithdrawStatusEnum[] status);

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts) AND "
        + "(DATE(w.requestDate) BETWEEN :startDate AND :endDate)")
    public Page<BaseMarketWithdraw> findByMarketAccountInAndRequestDateBetween(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts) AND "
        + "(DATE(w.requestDate) BETWEEN :startDate AND :endDate)")
    public List<BaseMarketWithdraw> findByMarketAccountInAndRequestDateBetween(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT SUM(w.amount) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND (DATE(w.requestDate) BETWEEN :startDate AND :endDate) AND w.status IN (:status)")
    public Integer sumAmountByMarketAccountInAndRequestDateBetweenAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") MarketWithdrawStatusEnum[] status);

    @Query(value = "SELECT SUM(w.withdrawFee) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND (DATE(w.requestDate) BETWEEN :startDate AND :endDate) AND w.status IN (:status)")
    public Integer sumWithdrawFeeByMarketAccountInAndRequestDateBetweenAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") MarketWithdrawStatusEnum[] status);

    @Query(value = "SELECT SUM(w.amount) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND (DATE(w.requestDate) BETWEEN :startDate AND :endDate)")
    public Integer sumAmountByMarketAccountInAndRequestDateBetween(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT SUM(w.withdrawFee) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND (DATE(w.requestDate) BETWEEN :startDate AND :endDate)")
    public Integer sumWithdrawFeeByMarketAccountInAndRequestDateBetween(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT w FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts) AND w.status IN (:status)")
    public Page<BaseMarketWithdraw> findByMarketAccountInAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("status") MarketWithdrawStatusEnum[] status,
        Pageable pageable);

    @Query(value = "SELECT SUM(w.amount) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND w.status IN (:status)")
    public Integer sumAmountByMarketAccountInAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("status") MarketWithdrawStatusEnum[] status);

    @Query(value = "SELECT SUM(w.withdrawFee) FROM BaseMarketWithdraw w WHERE w.marketAccount IN (:marketAccounts)"
        + " AND w.status IN (:status)")
    public Integer sumWithdrawFeeByMarketAccountInAndStatusIn(
        @Param("marketAccounts") List<? extends BaseMarketAccount> marketAccounts,
        @Param("status") MarketWithdrawStatusEnum[] status);

    public BaseMarketWithdraw findByMarketAccountAndToken(BaseMarketAccount marketAccount, String token);

    public BaseMarketWithdraw findByToken(String token);

    @Modifying
    @Query("UPDATE BaseMarketWithdraw SET status=:status, releaseDate=:releaseDate WHERE id=:id")
    public void updateStatusAndReleaseDateById(@Param("id") long id, @Param("status") MarketWithdrawStatusEnum status,
        @Param("releaseDate") LocalDateTime releaseDate);

    @Modifying
    @Query("UPDATE BaseMarketWithdraw SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") MarketWithdrawStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);

}
