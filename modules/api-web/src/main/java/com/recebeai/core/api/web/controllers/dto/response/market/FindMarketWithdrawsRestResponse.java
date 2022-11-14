package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

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
public class FindMarketWithdrawsRestResponse extends RestResponseDTO {

    private Long currentBalance;
    private List<MarketWithdrawDTO> withdraws;

    public FindMarketWithdrawsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, long currentBalance,
        Page<BaseMarketWithdraw> page) {
        super(page, resultCode);
        this.currentBalance = currentBalance;
        this.withdraws = dtoMapper.mapList(page.getContent(), MarketWithdrawDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("currentBalance", currentBalance)
            .add("withdraws.size", CollectionUtils.size(withdraws));
    }

}
