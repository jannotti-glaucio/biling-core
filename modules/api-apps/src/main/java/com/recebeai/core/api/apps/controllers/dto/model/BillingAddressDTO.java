package tech.jannotti.billing.core.api.apps.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingAddressDTO extends AbstractModelDTO {

    private String addressType;
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
            .add("street", street)
            .add("number", number)
            .add("complement", complement)
            .add("district", district)
            .add("zipCode", zipCode)
            .add("city", city)
            .add("state", state);
    }

}