package tech.jannotti.billing.core.api.web.controllers.dto.request.user;

import org.hibernate.validator.constraints.Email;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = 30)
    private String userName;

    @NotBlankParameter
    @ParameterLength(max = 60)
    @Email(message = CODE_INVALID_EMAIL_PARAMETER)
    private String email;

    @NotBlankParameter
    @ParameterLength(max = 100)
    private String realName;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("userName", userName)
            .add("email", email)
            .add("realName", realName);
    }

}