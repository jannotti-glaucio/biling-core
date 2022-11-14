package tech.jannotti.billing.core.api.web.controllers.dto.model.alert;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertTypeDTO extends AbstractModelDTO {
	
    private String code;
    private String name;
    
    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("code", code)
            .add("name", name);
    }

}
