package tech.jannotti.billing.core.persistence.repository.base.company;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CompanyBankAccountRepository extends AbstractRepository<BaseCompanyBankAccount, Long> {

    public List<BaseCompanyBankAccount> findByBank(BaseBank bank);

    public List<BaseCompanyBankAccount> findByCompanyAndStatus(BaseCompany company, EntityChildStatusEnum status);

    public BaseCompanyBankAccount getByCompanyAndToken(BaseCompany company, String token);

    public boolean existsByToken(String token);

}
