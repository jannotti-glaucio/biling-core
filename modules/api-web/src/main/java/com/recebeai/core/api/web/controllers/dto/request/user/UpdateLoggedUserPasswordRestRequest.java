package tech.jannotti.billing.core.api.web.controllers.dto.request.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.ValidationConstants;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

import lombok.Getter;

@Getter
public class UpdateLoggedUserPasswordRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String currentPassword;

    @NotBlankParameter
    @ParameterLength(max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String newPassword;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("oldPassword", currentPassword)
            .add("newPassword", newPassword);
    }

}
