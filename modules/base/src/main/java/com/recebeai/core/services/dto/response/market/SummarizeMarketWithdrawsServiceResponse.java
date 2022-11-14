package tech.jannotti.billing.core.services.dto.response.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.response.AbstractServiceResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummarizeMarketWithdrawsServiceResponse extends AbstractServiceResponse {

    private Integer totalAmount;
    private Integer totalFees;

    public SummarizeMarketWithdrawsServiceResponse(Integer totalAmount, Integer totalFees) {
        super();

        if (totalAmount != null)
            this.totalAmount = totalAmount;
        else
            this.totalAmount = 0;

        if (totalFees != null)
            this.totalFees = totalFees;
        else
            this.totalFees = 0;
    }

    public int getTotalNetAmount() {
        return totalAmount - totalFees;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("totalAmount", totalAmount)
            .add("totalFees", totalFees);
    }

}
