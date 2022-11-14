package tech.jannotti.billing.core.services.dto.response.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.response.AbstractServiceResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInvoiceFeesServiceResponse extends AbstractServiceResponse {

    private Integer fees;
    private Integer netAmount;

    public GetInvoiceFeesServiceResponse(int fees, int netAmount) {
        this.fees = fees;
        this.netAmount = netAmount;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
