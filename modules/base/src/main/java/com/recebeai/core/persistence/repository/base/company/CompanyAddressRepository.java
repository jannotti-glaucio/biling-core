package tech.jannotti.billing.core.persistence.repository.base.company;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyAddress;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CompanyAddressRepository extends AbstractRepository<BaseCompanyAddress, Long> {

    public BaseCompanyAddress getByCompanyAndBillingAddress(BaseCompany company, boolean billingAddress);

}