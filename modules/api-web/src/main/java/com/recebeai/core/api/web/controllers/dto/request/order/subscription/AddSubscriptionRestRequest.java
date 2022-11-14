package tech.jannotti.billing.core.api.web.controllers.dto.request.order.subscription;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidFutureDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidFrequencyType;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPaymentMethod;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSubscriptionRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidCustomer
    private String customer;

    @ParameterLength(max = 100)
    private String description;

    @ValidInteger
    @ParameterLength(max = 10)
    private String documentNumber;

    @NotBlankParameter
    @ValidFrequencyType
    private String frequencyType;

    @NotBlankParameter
    @ValidDate
    @ValidFutureDate
    private String startDate;

    @ValidDate
    @ValidFutureDate
    private String endDate;

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @NotBlankParameter
    @ValidPaymentMethod
    private String paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("frequencyType", frequencyType)
            .add("startDate", startDate)
            .add("endDate", endDate)
            .add("amount", amount)
            .add("paymentMethod", paymentMethod);
    }

}
