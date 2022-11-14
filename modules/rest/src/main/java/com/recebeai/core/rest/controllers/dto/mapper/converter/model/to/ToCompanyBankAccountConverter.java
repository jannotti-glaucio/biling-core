package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.AbstractToModelConverter;
import tech.jannotti.billing.core.services.bank.BankingService;

@Component
public class ToCompanyBankAccountConverter extends AbstractToModelConverter<BaseCompanyBankAccount> {

    @Autowired
    private BankingService bankingService;

    @Override
    protected BaseCompanyBankAccount convert(String source) {
        if (source == null)
            return null;

        BaseCompany company = getLoggedCompany();
        return bankingService.getCompanyBankAccount(company, source);
    }

}
