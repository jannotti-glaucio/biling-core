package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.companyBankAccountRepository")
public interface CompanyBankAccountRepository extends AbstractRepository<SantanderCompanyBankAccount, Long> {

    public SantanderCompanyBankAccount getByBaseCompanyBankAccount(BaseCompanyBankAccount baseCompanyBankAccount);

}
