package tech.jannotti.billing.core.api.web.controllers.dto.model.order;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdertShortDTO extends AbstractModelDTO {

    private String token;
    private String description;
    private Long documentNumber;
    private String orderType;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("orderType", orderType);
    }

}
