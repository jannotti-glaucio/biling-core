package tech.jannotti.billing.core.persistence.repository.base.market;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;

@Repository
public interface MarketAccountRepository extends AbstractMarketAccountRepository<BaseMarketAccount> {

    public List<BaseMarketAccount> findByStatus(EntityStatusEnum status);

}
