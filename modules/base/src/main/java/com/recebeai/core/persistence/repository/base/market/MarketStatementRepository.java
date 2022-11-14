package tech.jannotti.billing.core.persistence.repository.base.market;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.MarketStatementDirectionEnum;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatement;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface MarketStatementRepository extends AbstractRepository<BaseMarketStatement, Long> {

    public List<BaseMarketStatement> findByMarketAccountAndStatementDateBetweenOrderByStatementDateAscIdAsc(
        BaseMarketAccount marketAccount, LocalDate startStatementDate, LocalDate endStatementDate);

    @Query(value = "SELECT SUM(s.amount) FROM BaseMarketStatement s WHERE s.marketAccount=:marketAccount"
        + " AND s.type.direction = :direction AND s.statementDate>:statementDate)")
    public Integer sumAmountByMarketAccountAndDirectionAndStatementDateGreaterThan(
        @Param("marketAccount") BaseMarketAccount marketAccount,
        @Param("direction") MarketStatementDirectionEnum direction,
        @Param("statementDate") LocalDate statementDate);

    @Query(value = "SELECT SUM(s.amount) FROM BaseMarketStatement s WHERE s.marketAccount=:marketAccount"
        + " AND s.type.direction = :direction AND s.statementDate<:statementDate)")
    public Integer sumAmountByMarketAccountAndDirectionAndStatementDateLessThan(
        @Param("marketAccount") BaseMarketAccount marketAccount,
        @Param("direction") MarketStatementDirectionEnum direction,
        @Param("statementDate") LocalDate statementDate);

    @Query(value = "SELECT SUM(s.amount) FROM BaseMarketStatement s WHERE s.marketAccount=:marketAccount"
        + " AND s.type.direction = :direction AND s.statementDate=:statementDate)")
    public Integer sumAmountByMarketAccountAndDirectionAndStatementDate(
        @Param("marketAccount") BaseMarketAccount marketAccount,
        @Param("direction") MarketStatementDirectionEnum direction,
        @Param("statementDate") LocalDate statementDate);

    @Query(value = "SELECT MIN(s.statementDate) FROM BaseMarketStatement s WHERE s.marketAccount=:marketAccount")
    public LocalDate minimumStatementDateByMarketAccount(@Param("marketAccount") BaseMarketAccount marketAccount);

}
