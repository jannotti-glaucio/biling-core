package tech.jannotti.billing.core.persistence.repository.base.company;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CompanyRepository extends AbstractRepository<BaseCompany, Integer> {

}
