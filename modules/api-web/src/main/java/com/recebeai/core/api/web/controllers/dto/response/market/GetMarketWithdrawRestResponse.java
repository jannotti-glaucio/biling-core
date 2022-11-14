package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.market.MarketWithdrawDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMarketWithdrawRestResponse extends RestResponseDTO {

    private MarketWithdrawDTO withdraw;

    public GetMarketWithdrawRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseMarketWithdraw withdraw) {
        super(resultCode);
        this.withdraw = dtoMapper.map(withdraw, MarketWithdrawDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("withdraws", withdraw);
    }

}
