package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.companyBankAccountRepository")
public interface CompanyBankAccountRepository extends AbstractRepository<BancoBrasilCompanyBankAccount, Long> {

    public BancoBrasilCompanyBankAccount getByBaseCompanyBankAccount(BaseCompanyBankAccount baseCompanyBankAccount);

}
