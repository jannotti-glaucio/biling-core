package tech.jannotti.billing.core.api.web.controllers.dto.request.order.subscription;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDay;
import tech.jannotti.billing.core.validation.extension.annotations.ValidFutureDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubscriptionRestRequest extends AbstractRestRequestDTO {

    @ParameterLength(max = 100)
    private String description;

    @ValidInteger
    @ParameterLength(max = 10)
    private String documentNumber;

    @ValidDay
    private String expirationDay;

    @ValidDate
    @ValidFutureDate
    private String endDate;

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("endDate", endDate)
            .add("amount", amount);
    }

}
