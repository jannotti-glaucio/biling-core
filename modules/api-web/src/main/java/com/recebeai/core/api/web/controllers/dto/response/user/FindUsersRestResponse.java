package tech.jannotti.billing.core.api.web.controllers.dto.response.user;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

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
public class FindUsersRestResponse extends RestResponseDTO {

    private List<UserDTO> users;

    public FindUsersRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<? extends BaseUser> page) {
        super(resultCode);
        this.users = dtoMapper.mapList(page, UserDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("users.size", CollectionUtils.size(users));
    }

}
