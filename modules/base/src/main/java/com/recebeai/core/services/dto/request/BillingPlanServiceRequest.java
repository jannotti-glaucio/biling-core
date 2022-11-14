package tech.jannotti.billing.core.services.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingPlanServiceRequest extends AbstractServiceRequest {

    private String description;
    private Integer paidBankBilletFee;
    private Integer marketWithdrawFee;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("paidBankBilletFee", paidBankBilletFee)
            .add("marketWithdrawFee", marketWithdrawFee);
    }

}
