package tech.jannotti.billing.core.services.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.services.AbstractService;

@Service
public class CompanyBankAccountService extends AbstractService {

    @Autowired
    private CompanyBankAccountRepository bankAccountRepository;

    List<BaseCompanyBankAccount> findActiveAccounts(BaseCompany company) {
        return bankAccountRepository.findByCompanyAndStatus(company, EntityChildStatusEnum.ACTIVE);
    }

}
