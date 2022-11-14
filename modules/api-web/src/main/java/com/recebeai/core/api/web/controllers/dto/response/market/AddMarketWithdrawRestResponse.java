package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddMarketWithdrawRestResponse extends RestResponseDTO {

    private String token;
    private Long currentBalance;

    public AddMarketWithdrawRestResponse(BaseResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }

    public AddMarketWithdrawRestResponse(BaseResultCode resultCode, long currentBalance) {
        super(resultCode);
        this.currentBalance = currentBalance;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("currentBalance", currentBalance);
    }

}
