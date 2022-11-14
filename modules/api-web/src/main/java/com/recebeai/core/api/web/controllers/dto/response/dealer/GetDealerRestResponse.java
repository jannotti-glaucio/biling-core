package tech.jannotti.billing.core.api.web.controllers.dto.response.dealer;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.dealer.DealerDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetDealerRestResponse extends RestResponseDTO {

	private Long currentBalance;
	private DealerDTO dealer;

    public GetDealerRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, long currentBalance, BaseDealer dealer) {
        super(resultCode);
        this.currentBalance = currentBalance;
        this.dealer = dtoMapper.map(dealer, DealerDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
        	.add("currentBalance", currentBalance)
            .add("dealer", dealer);
    }
}
