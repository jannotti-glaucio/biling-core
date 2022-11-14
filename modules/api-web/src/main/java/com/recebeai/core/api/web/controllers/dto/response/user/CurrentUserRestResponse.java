package tech.jannotti.billing.core.api.web.controllers.dto.response.user;

import java.util.Map;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.user.UserDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class CurrentUserRestResponse extends RestResponseDTO {

    private UserDTO user;
    private Map<String, Object> environment;

    public CurrentUserRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseUser user,
        Map<String, Object> environment) {
        super(resultCode);
        this.user = dtoMapper.map(user, UserDTO.class);
        this.environment = environment;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("user", user)
            .add("enviroment", environment);
    }

}
