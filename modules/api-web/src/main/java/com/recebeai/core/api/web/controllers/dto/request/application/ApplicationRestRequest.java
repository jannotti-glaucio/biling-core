package tech.jannotti.billing.core.api.web.controllers.dto.request.application;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = 50)
    private String name;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("name", name);
    }

}