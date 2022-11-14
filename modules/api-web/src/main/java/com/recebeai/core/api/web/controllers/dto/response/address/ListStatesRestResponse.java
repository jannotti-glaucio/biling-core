package tech.jannotti.billing.core.api.web.controllers.dto.response.address;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.StateDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseState;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class ListStatesRestResponse extends RestResponseDTO {

    private List<StateDTO> states;

    public ListStatesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseState> states) {
        super(resultCode);
        this.states = dtoMapper.mapList(states, StateDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("states.size", CollectionUtils.size(states));
    }

}