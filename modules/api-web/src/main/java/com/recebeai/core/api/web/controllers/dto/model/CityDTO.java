package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityDTO extends AbstractModelDTO {

    private String state;
    private String name;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("state", state)
            .add("name", name);
    }

}