package tech.jannotti.billing.core.api.web.controllers.dto.response.address;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.CityDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseCity;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class ListCitiesRestResponse extends RestResponseDTO {

    private List<CityDTO> cities;

    public ListCitiesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseCity> cities) {
        super(resultCode);
        this.cities = dtoMapper.mapList(cities, CityDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("cities.size", CollectionUtils.size(cities));
    }

}