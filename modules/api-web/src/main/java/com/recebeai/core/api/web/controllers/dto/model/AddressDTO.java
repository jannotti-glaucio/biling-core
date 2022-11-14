package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO extends AbstractModelDTO {

    private String token;
    private String addressType;
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
            .add("token", token)
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