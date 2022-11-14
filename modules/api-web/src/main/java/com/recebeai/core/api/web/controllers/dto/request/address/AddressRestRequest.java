package tech.jannotti.billing.core.api.web.controllers.dto.request.address;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidBoolean;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidAddressType;
import tech.jannotti.billing.core.validation.extension.groups.AddValidations;
import tech.jannotti.billing.core.validation.extension.groups.UpdateValidations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ValidAddressType
    private String addressType;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ValidBoolean
    private String billingAddress;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 80)
    private String street;

    @ParameterLength(max = 10)
    private String number;

    @ParameterLength(max = 20)
    private String complement;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 30)
    private String district;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ValidInteger
    @ParameterLength(max = 8)
    private String zipCode;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 50)
    private String city;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 2)
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