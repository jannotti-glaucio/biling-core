package tech.jannotti.billing.core.services.dto.response.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.response.AbstractServiceResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummarizeInvoicesServiceResponse extends AbstractServiceResponse {

    private Integer totalFees;
    private Integer totalPaidAmount;

    public SummarizeInvoicesServiceResponse(Integer totalFees, Integer totalPaidAmount) {
        super();

        if (totalFees != null)
            this.totalFees = totalFees;
        else
            this.totalFees = 0;

        if (totalPaidAmount != null)
            this.totalPaidAmount = totalPaidAmount;
        else
            this.totalPaidAmount = 0;
    }

    public int getTotalNetAmount() {
        return totalPaidAmount - totalFees;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("totalFees", totalFees)
            .add("totalPaidAmount", totalPaidAmount);
    }

}
