package tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping;

import org.modelmapper.PropertyMap;

import tech.jannotti.billing.core.api.web.controllers.dto.model.market.MarketStatementTypeDTO;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatementType;

public class ToMarketStatementTypeDTOMapping extends PropertyMap<BaseMarketStatementType, MarketStatementTypeDTO> {

    protected void configure() {
        map().setDescription(source.getDescription());
    }
}
