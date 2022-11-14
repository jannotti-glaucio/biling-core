package tech.jannotti.billing.core.api.web.controllers.dto.model.dealer;

import tech.jannotti.billing.core.api.web.controllers.dto.model.market.MarketAccountDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerMarketAccountDTO extends MarketAccountDTO {

    private DealerShortDTO dealer;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("dealer", dealer);
    }

}
