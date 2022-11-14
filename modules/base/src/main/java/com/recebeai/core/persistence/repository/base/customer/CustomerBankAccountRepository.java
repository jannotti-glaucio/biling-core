package tech.jannotti.billing.core.persistence.repository.base.customer;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerBankAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CustomerBankAccountRepository extends AbstractRepository<BaseCustomerBankAccount, Long> {

}
