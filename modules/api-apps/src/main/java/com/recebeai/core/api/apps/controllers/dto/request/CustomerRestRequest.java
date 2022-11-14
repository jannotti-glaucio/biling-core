package tech.jannotti.billing.core.api.apps.controllers.dto.request;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPersonType;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDocumentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = 100)
    private String name;

    @NotBlankParameter
    @ValidPersonType
    private String personType;

    @NotBlankParameter
    @ValidDocumentType
    private String documentType;

    @NotBlankParameter
    @ValidInteger
    @ParameterLength(max = 20)
    private String documentNumber;

    @ParameterLength(max = 15)
    public String phoneNumber;

    @ParameterLength(max = 15)
    public String mobileNumber;

    @Email(message = CODE_INVALID_EMAIL_PARAMETER)
    @ParameterLength(max = 60)
    private String email;

    private String comments;

    @Valid
    private AddressRestRequest billingAddress;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("name", name)
            .add("personType", personType)
            .add("documentType", documentType)
            .add("documentNumber", documentNumber)
            .add("phoneNumber", phoneNumber)
            .add("mobileNumber", mobileNumber)
            .add("email", email)
            .add("comments", comments)
            .add("billingAddress", billingAddress);
    }

}