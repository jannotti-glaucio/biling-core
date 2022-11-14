package tech.jannotti.billing.core.api.web.controllers.dto.model.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceShortDTO extends AbstractModelDTO {

    private String token;
    private Integer amount;
    private String instalment;
    private String expirationDate;
    private String paymentMethod;
    private String status;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("amount", amount)
            .add("instalment", instalment)
            .add("expirationDate", expirationDate)
            .add("paymentMethod", paymentMethod)
            .add("status", status);
    }

}
