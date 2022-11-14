package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMarketBalanceRestResponse extends RestResponseDTO {

    private Long currentBalance;

    public GetMarketBalanceRestResponse(BaseResultCode resultCode, long currentBalance) {
        super(resultCode);
        this.currentBalance = currentBalance;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("currentBalance", currentBalance);
    }

}
