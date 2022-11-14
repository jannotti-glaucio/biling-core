package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO extends AbstractModelDTO {

    private String token;
    private String name;
    private String clientId;
    private String status;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("name", name)
            .add("clientId", clientId)
            .add("status", status);
    }

}
