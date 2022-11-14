package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.AbstractToModelConverter;
import tech.jannotti.billing.core.services.customer.CustomerService;

@Component
public class ToCustomerConverter extends AbstractToModelConverter<BaseCustomer> {

    @Autowired
    private CustomerService customerService;

    @Override
    protected BaseCustomer convert(String source) {
        if (source == null)
            return null;

        BaseDealer dealer = getLoggedDealer();
        return customerService.get(dealer, source);
    }

}
