package tech.jannotti.billing.core.api.apps.controllers.dto.request.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInvoiceRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidCustomer
    private String customer;

    @ParameterLength(max = 100)
    private String description;

    @ValidInteger
    @ParameterLength(max = 10)
    private String documentNumber;

    @NotBlankParameter
    @ValidDate
    private String expirationDate;

    @NotBlankParameter
    @ValidInteger
    @ParameterLength(max = 10)
    private String amount;

    @ValidDate
    private String penaltyStartDate;

    @NotBlankParameter
    @ParameterLength(max = 200)
    private String callbackUrl;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("penaltyStartDate", penaltyStartDate)
            .add("callbackUrl", callbackUrl);
    }

}
