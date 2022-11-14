package tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrderCollectionInstalmentDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.services.dto.model.OrderInstalmentServiceDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCollectionInstalmentsRestResponse extends RestResponseDTO {

    private List<OrderCollectionInstalmentDTO> instalments;

    public GetCollectionInstalmentsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<OrderInstalmentServiceDTO> instalments) {
        super(resultCode);
        this.instalments = dtoMapper.mapList(instalments, OrderCollectionInstalmentDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("instalments.size", CollectionUtils.size(instalments));
    }

}
