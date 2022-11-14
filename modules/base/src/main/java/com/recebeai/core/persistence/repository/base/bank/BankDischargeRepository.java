package tech.jannotti.billing.core.persistence.repository.base.bank;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankDischarge;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface BankDischargeRepository extends AbstractRepository<BaseBankDischarge, Long> {

}