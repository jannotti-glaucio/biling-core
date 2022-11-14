package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceDetail;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.cnab400RemittanceDetailRepository")
public interface CNAB400RemittanceDetailRepository extends AbstractRepository<SantanderCNAB400RemittanceDetail, Long> {

}
