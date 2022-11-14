package tech.jannotti.billing.core.services.dto.request.address;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressServiceRequest extends AddressServiceRequest {

    private CRUDOperationEnum operation;
    private String token;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token);
    }

}
