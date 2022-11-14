package tech.jannotti.billing.core.api.web.controllers.dto.model.alert;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceAlertDTO extends AbstractModelDTO {

    private String mediaType;
    private AlertTypeDTO alertType;
    private String creationDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("mediaType", mediaType)
            .add("alertType", alertType)
            .add("creationDate", creationDate);
    }

}
