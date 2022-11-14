package tech.jannotti.billing.core.api.web.controllers.dto.model.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketStatementTypeDTO extends AbstractModelDTO {

    private String code;
    private String description;
    private String direction;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("code", code)
            .add("description", description)
            .add("direction", direction);
    }

}
