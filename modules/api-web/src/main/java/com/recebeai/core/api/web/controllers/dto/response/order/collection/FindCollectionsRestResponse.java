package tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrderCollectionDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class FindCollectionsRestResponse extends RestResponseDTO {

    private List<OrderCollectionDTO> collections;

    public FindCollectionsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseOrderCollection> page) {
        super(page, resultCode);
        this.collections = dtoMapper.mapList(page.getContent(), OrderCollectionDTO.class);
    }

    public FindCollectionsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<BaseOrderCollection> collections) {
        super(resultCode);
        this.collections = dtoMapper.mapList(collections, OrderCollectionDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("collections.size", CollectionUtils.size(collections));
    }

}