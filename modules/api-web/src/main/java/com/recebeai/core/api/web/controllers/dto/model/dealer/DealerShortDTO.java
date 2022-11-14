package tech.jannotti.billing.core.api.web.controllers.dto.model.dealer;

import tech.jannotti.billing.core.api.web.controllers.dto.model.DocumentTypeDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.company.CompanyShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerShortDTO extends AbstractModelDTO {

    private String token;
    private String name;
    private CompanyShortDTO company;
    private String personType;
    private DocumentTypeDTO documentType;
    private String documentNumber;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("name", name)
            .add("company", company)
            .add("personType", personType)
            .add("documentType", documentType)
            .add("documentNumber", documentNumber);
    }

}