package tech.jannotti.billing.core.services.dto.request.address;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.AddressTypeEnum;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressServiceRequest extends AbstractServiceRequest {

    private AddressTypeEnum addressType;
    private boolean billingAddress;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String zipCode;
    private String city;
    private String state;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("addressType", addressType)
            .add("billingAddress", billingAddress)
            .add("street", street)
            .add("number", number)
            .add("complement", complement)
            .add("district", district)
            .add("zipCode", zipCode)
            .add("city", city)
            .add("state", state);
    }

}
