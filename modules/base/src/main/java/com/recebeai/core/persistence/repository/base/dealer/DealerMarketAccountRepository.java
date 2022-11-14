package tech.jannotti.billing.core.persistence.repository.base.dealer;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerMarketAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface DealerMarketAccountRepository extends AbstractRepository<BaseDealerMarketAccount, Long> {

    public List<BaseDealerMarketAccount> findByDealerCompanyAndStatus(BaseCompany company, EntityStatusEnum status);

    public BaseDealerMarketAccount getByDealerAndStatus(BaseDealer dealer, EntityStatusEnum status);

}
