package tech.jannotti.billing.core.api.web.controllers.dto.request.customer;

import java.util.List;

import javax.validation.Valid;

import tech.jannotti.billing.core.api.web.controllers.dto.request.address.AddressRestRequest;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCustomerRestRequest extends AbstractCustomerRestRequest {

    @Valid
    private List<AddressRestRequest> addresses;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("addresses", addresses);
    }

}