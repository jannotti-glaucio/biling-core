package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.AbstractToModelConverter;
import tech.jannotti.billing.core.services.bank.BankingService;

@Component
public class ToDealerBankAccountConverter extends AbstractToModelConverter<BaseDealerBankAccount> {

    @Autowired
    private BankingService bankingService;

    @Override
    protected BaseDealerBankAccount convert(String source) {
        if (source == null)
            return null;

        BaseDealer dealer = getLoggedDealer();
        return bankingService.getDealerBankAccount(dealer, source);
    }

}
