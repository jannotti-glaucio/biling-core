package tech.jannotti.billing.core.services.dto.request.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserServiceRequest extends UpdateUserServiceRequest {

    private String password;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .mask("password", password);
    }

}