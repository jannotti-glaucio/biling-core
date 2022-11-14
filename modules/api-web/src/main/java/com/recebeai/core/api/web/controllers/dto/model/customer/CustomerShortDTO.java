package tech.jannotti.billing.core.api.web.controllers.dto.model.customer;

import tech.jannotti.billing.core.api.web.controllers.dto.model.DocumentTypeDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerShortDTO extends AbstractModelDTO {

    private String token;
    private String name;
    private DocumentTypeDTO documentType;
    private String documentNumber;
    private String email;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("name", name)
            .add("documentType", documentType)
            .add("documentNumber", documentNumber)
            .add("email", email);
    }

}