package tech.jannotti.billing.core.persistence.repository.base.market;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketBalance;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface MarketBalanceRepository extends AbstractRepository<BaseMarketBalance, Long> {

    public BaseMarketBalance getFirstByMarketAccountOrderByBalanceDateDesc(BaseMarketAccount marketAccount);

    public BaseMarketBalance getFirstByMarketAccountAndBalanceDateLessThanOrderByBalanceDateDesc(BaseMarketAccount marketAccount,
        LocalDate balanceDate);

}
