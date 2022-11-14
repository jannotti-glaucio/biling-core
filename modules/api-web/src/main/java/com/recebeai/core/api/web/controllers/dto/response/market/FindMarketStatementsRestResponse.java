package tech.jannotti.billing.core.api.web.controllers.dto.response.market;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.market.MarketStatementDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatement;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMarketStatementsRestResponse extends RestResponseDTO {

    private Long currentBalance;
    private List<MarketStatementDTO> statements;

    public FindMarketStatementsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, long currentBalance,
        List<BaseMarketStatement> statements) {
        super(resultCode);
        this.currentBalance = currentBalance;
        this.statements = dtoMapper.mapList(statements, MarketStatementDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("currentBalance", currentBalance)
            .add("statements.size", CollectionUtils.size(statements));
    }

}
