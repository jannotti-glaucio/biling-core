package tech.jannotti.billing.core.api.apps.controllers.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidAddressType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidAddressType
    private String addressType;

    @NotBlankParameter
    @ParameterLength(max = 80)
    private String street;

    @ParameterLength(max = 10)
    private String number;

    @ParameterLength(max = 20)
    private String complement;

    @NotBlankParameter
    @ParameterLength(max = 30)
    private String district;

    @NotBlankParameter
    @ValidInteger
    @ParameterLength(max = 8)
    private String zipCode;

    @NotBlankParameter
    @ParameterLength(max = 50)
    private String city;

    @NotBlankParameter
    @ParameterLength(max = 2)
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