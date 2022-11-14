package tech.jannotti.billing.core.services.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.services.AbstractService;

@Service
public class CompanyService extends AbstractService {

    @Autowired
    private CompanyBankAccountService bankAccountService;

    public List<BaseCompanyBankAccount> getBankAccounts(BaseCompany company) {
        List<BaseCompanyBankAccount> bankAccounts = bankAccountService.findActiveAccounts(company);
        return bankAccounts;
    }

}
