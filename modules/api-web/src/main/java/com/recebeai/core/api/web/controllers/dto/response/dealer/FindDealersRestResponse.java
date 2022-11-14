package tech.jannotti.billing.core.api.web.controllers.dto.response.dealer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

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
public class FindDealersRestResponse extends RestResponseDTO {

    private List<DealerDTO> dealers;

    public FindDealersRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseDealer> page) {
        super(page, resultCode);
        this.dealers = dtoMapper.mapList(page.getContent(), DealerDTO.class);
    }

    public FindDealersRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseDealer> dealers) {
        super(resultCode);
        this.dealers = dtoMapper.mapList(dealers, DealerDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("dealers.size", CollectionUtils.size(dealers));
    }
}
