package tech.jannotti.billing.core.api.web.controllers.dto.request.billingPlan;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingPlanRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = 30)
    private String description;

    @NotBlankParameter
    @ValidInteger
    private String paidBankBilletFee;

    @NotBlankParameter
    @ValidInteger
    private String marketWithdrawFee;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("paidBankBilletFee", paidBankBilletFee)
            .add("marketWithdrawFee", marketWithdrawFee);
    }

}
