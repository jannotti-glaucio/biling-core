package tech.jannotti.billing.core.api.web.controllers.dto.request.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.validation.ValidationConstants;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRestRequest extends UpdateUserRestRequest {

    @NotBlankParameter
    @ParameterLength(max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String password;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .mask("password", password);
    }

}