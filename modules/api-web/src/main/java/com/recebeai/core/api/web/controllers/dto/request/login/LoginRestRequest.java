package tech.jannotti.billing.core.api.web.controllers.dto.request.login;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    private String username;

    @NotBlankParameter
    private String password;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("username", username)
            .mask("password", password);
    }

}
