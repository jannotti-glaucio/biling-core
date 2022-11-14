package tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrderCollectionDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class GetCollectionRestResponse extends RestResponseDTO {

    private OrderCollectionDTO collection;

    public GetCollectionRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseOrderCollection collection) {
        super(resultCode);
        this.collection = dtoMapper.map(collection, OrderCollectionDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("collection", collection);
    }

}