package tech.jannotti.billing.core.api.web.controllers.dto.model.payment;

import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentShortDTO extends AbstractModelDTO {

    private String token;
    private InvoiceShortDTO invoice;
    private String paymentMethod;
    private Integer amount;
    private Integer paidAmount;
    private Integer fees;
    private Integer netAmount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("invoice", invoice)
            .add("paymentMethod", paymentMethod)
            .add("amount", amount)
            .add("paidAmount", paidAmount)
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
