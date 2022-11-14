package tech.jannotti.billing.core.services.dto.request.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserServiceRequest extends AbstractServiceRequest {

    private String userName;
    private String email;
    private String realName;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("userName", userName)
            .add("email", email)
            .add("realName", realName);
    }

}