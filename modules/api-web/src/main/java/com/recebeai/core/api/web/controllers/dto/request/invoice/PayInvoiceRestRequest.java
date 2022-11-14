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
public class PayInvoiceRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidDate
    private String paymentDate;

    @NotBlankParameter
    @ValidInteger
    @ParameterLength(max = 10)
    private String fee;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("paymentDate", paymentDate)
            .add("fee", fee);
    }

}
