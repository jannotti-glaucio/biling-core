package tech.jannotti.billing.core.persistence.repository.base.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.user.BaseCompanyUser;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CompanyUserRepository extends AbstractRepository<BaseCompanyUser, Long> {

    public List<BaseCompanyUser> getByCompanyAndMarketWithdrawNotificationsTrue(BaseCompany company);
}
