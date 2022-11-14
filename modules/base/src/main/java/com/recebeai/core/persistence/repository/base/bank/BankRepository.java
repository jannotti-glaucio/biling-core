package tech.jannotti.billing.core.persistence.repository.base.bank;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface BankRepository extends AbstractRepository<BaseBank, Integer> {

    public BaseBank getByCode(String code);

}
