package tech.jannotti.billing.core.api.web.controllers.dto.request.customer;

import java.util.List;

import javax.validation.Valid;

import tech.jannotti.billing.core.api.web.controllers.dto.request.address.UpdateAddressRestRequest;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRestRequest extends AbstractCustomerRestRequest {

    @Valid
    private List<UpdateAddressRestRequest> addresses;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("addresses", addresses);
    }

}