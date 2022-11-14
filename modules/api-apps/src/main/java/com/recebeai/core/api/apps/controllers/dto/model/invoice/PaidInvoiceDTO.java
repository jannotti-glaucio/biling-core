package tech.jannotti.billing.core.api.apps.controllers.dto.model.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaidInvoiceDTO extends AbstractModelDTO {

    private String token;
    private String documentNumber;
    private String paymentMethod;
    private Integer paidAmount;
    private Integer fees;
    private Integer netAmount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("documentNumber", documentNumber)
            .add("paymentMethod", paymentMethod)
            .add("paidAmount", paidAmount)
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
