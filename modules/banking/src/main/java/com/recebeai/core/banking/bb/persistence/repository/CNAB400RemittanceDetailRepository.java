package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400RemittanceDetail;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.cnab400RemittanceDetailRepository")
public interface CNAB400RemittanceDetailRepository extends AbstractRepository<BancoBrasilCNAB400RemittanceDetail, Long> {

}
