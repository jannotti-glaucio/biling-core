package tech.jannotti.billing.core.api.web.controllers.dto.response.user;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.user.UserDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserRestResponse extends RestResponseDTO {

    private UserDTO user;

    public GetUserRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseUser user) {
        super(resultCode);
        this.user = dtoMapper.map(user, UserDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("user", user);
    }

}
