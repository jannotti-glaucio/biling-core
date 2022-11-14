package tech.jannotti.billing.core.api.apps.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends AbstractModelDTO {

    private String token;
    private String name;
    private String personType;
    private String documentType;
    private String documentNumber;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String comments;
    private String status;
    private BillingAddressDTO billingAddress;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("name", name)
            .add("personType", personType)
            .add("documentType", documentType)
            .add("documentNumber", documentNumber)
            .add("phoneNumber", phoneNumber)
            .add("mobileNumber", mobileNumber)
            .add("email", email)
            .add("comments", comments)
            .add("status", status)
            .add("billingAddress", billingAddress);
    }

}