package tech.jannotti.billing.core.persistence.repository.base.customer;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerMarketAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CustomerMarketAccountRepository extends AbstractRepository<BaseCustomerMarketAccount, Long> {

}
