package tech.jannotti.billing.core.api.web.controllers.dto.model.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShortDTO extends AbstractModelDTO {

    private String token;
    private String username;
    private String email;
    private String realName;
    private String status;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("username", username)
            .add("email", email)
            .add("realName", realName)
            .add("status", status);
    }

}