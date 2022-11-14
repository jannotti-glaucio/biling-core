package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.services.bank.BankingService;

@Component
public class ToBankConverter extends AbstractConverter<String, BaseBank> {

    @Autowired
    private BankingService bankingService;

    @Override
    protected BaseBank convert(String source) {
        if (source == null)
            return null;

        return bankingService.getBank(source);
    }

}
