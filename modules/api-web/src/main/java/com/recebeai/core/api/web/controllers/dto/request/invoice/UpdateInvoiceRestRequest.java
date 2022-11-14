package tech.jannotti.billing.core.api.web.controllers.dto.request.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceRestRequest extends AbstractRestRequestDTO {

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

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("penaltyStartDate", penaltyStartDate);
    }

}
