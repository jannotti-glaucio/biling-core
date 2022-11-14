package tech.jannotti.billing.core.api.apps.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBankBilletDTO extends AbstractModelDTO {

    private String lineCode;
    private String url;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("lineCode", lineCode)
            .add("url", url);
    }

}
