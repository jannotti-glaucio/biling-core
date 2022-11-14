package tech.jannotti.billing.core.services.dto.response.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.response.AbstractServiceResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMarketWithdrawFeesServiceResponse extends AbstractServiceResponse {

    private Integer fees;
    private Integer netAmount;

    public GetMarketWithdrawFeesServiceResponse(int fees, int netAmount) {
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
