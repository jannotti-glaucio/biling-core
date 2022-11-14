package tech.jannotti.billing.core.api.web.controllers.dto.request.order.collection;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPaymentMethod;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidCollectionAmountType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCollectionInstalmentsRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidInteger
    private String instalments;

    @NotBlankParameter
    @ValidDate
    private String expirationDate;

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @NotBlankParameter
    @ValidCollectionAmountType
    private String amountType;

    @NotBlankParameter
    @ValidPaymentMethod
    private String paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("instalments", instalments)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("amountType", amountType)
            .add("paymentMethod", paymentMethod);
    }

}
