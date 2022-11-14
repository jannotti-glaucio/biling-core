package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.dealer.DealerMarketWithdrawDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindDealerMarketWithdrawsRestResponse extends RestResponseDTO {

    private List<DealerMarketWithdrawDTO> withdraws;
    private Integer totalAmount;
    private Integer totalFees;
    private Integer totalNetAmount;

    public FindDealerMarketWithdrawsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        Page<BaseMarketWithdraw> page) {
        super(page, resultCode);
        this.withdraws = dtoMapper.mapList(page.getContent(), DealerMarketWithdrawDTO.class);
    }

    public FindDealerMarketWithdrawsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        Page<BaseMarketWithdraw> page, int totalAmount, int totalFees, int totalNetAmount) {
        super(page, resultCode);
        this.withdraws = dtoMapper.mapList(page.getContent(), DealerMarketWithdrawDTO.class);
        this.totalAmount = totalAmount;
        this.totalFees = totalFees;
        this.totalNetAmount = totalNetAmount;
    }

    public FindDealerMarketWithdrawsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<BaseMarketWithdraw> withdraws, int totalAmount, int totalFees, int totalNetAmount) {
        super(resultCode);
        this.withdraws = dtoMapper.mapList(withdraws, DealerMarketWithdrawDTO.class);
        this.totalAmount = totalAmount;
        this.totalFees = totalFees;
        this.totalNetAmount = totalNetAmount;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("withdraws.size", CollectionUtils.size(withdraws))
            .add("totalAmount", totalAmount)
            .add("totalFees", totalFees)
            .add("totalNetAmount", totalNetAmount);
    }
}
