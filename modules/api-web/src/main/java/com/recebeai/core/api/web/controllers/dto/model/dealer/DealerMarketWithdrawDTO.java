package tech.jannotti.billing.core.api.web.controllers.dto.model.dealer;

import tech.jannotti.billing.core.api.web.controllers.dto.model.market.MarketWithdrawDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerMarketWithdrawDTO extends MarketWithdrawDTO {

    private DealerMarketAccountDTO marketAccount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("marketAccount", marketAccount);
    }

}
