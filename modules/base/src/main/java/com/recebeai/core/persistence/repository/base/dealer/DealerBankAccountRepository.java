package tech.jannotti.billing.core.persistence.repository.base.dealer;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.bank.AbstractBankAccountRepository;

@Repository
public interface DealerBankAccountRepository extends AbstractBankAccountRepository<BaseDealerBankAccount> {

    public BaseDealerBankAccount getByDealerAndToken(BaseDealer dealer, String token);

    public BaseDealerBankAccount getByDealerAndTokenAndStatus(BaseDealer dealer, String token, EntityChildStatusEnum status);

    public List<BaseDealerBankAccount> findByDealerAndStatus(BaseDealer dealer, EntityChildStatusEnum status);

}
