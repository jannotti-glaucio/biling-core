package tech.jannotti.billing.core.api.web.controllers.dto.request.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInvoiceFeesRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @NotBlankParameter
    @ValidPaymentMethod
    private String paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("amount", amount)
            .add("paymentMethod", paymentMethod);
    }

}
