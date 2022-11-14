package tech.jannotti.billing.core.services.dto.response.payment;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.response.AbstractServiceResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummarizePaymentsServiceResponse extends AbstractServiceResponse {

    private Integer companyFee;
    private Integer paidAmount;

    public SummarizePaymentsServiceResponse(Integer companyFee, Integer paidAmount) {
        super();

        if (companyFee != null)
            this.companyFee = companyFee;
        else
            this.companyFee = 0;

        if (paidAmount != null)
            this.paidAmount = paidAmount;
        else
            this.paidAmount = 0;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("companyFee", companyFee)
            .add("paidAmount", paidAmount);
    }

}
