package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentTypeDTO extends AbstractModelDTO {

    private String personType;
    private String code;
    private String name;
    private String webMask;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("personType", personType)
            .add("code", code)
            .add("name", name)
            .add("webMask", webMask);
    }

}