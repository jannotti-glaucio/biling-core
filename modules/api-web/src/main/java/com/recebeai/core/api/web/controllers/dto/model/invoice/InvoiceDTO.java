package tech.jannotti.billing.core.api.web.controllers.dto.model.invoice;

import tech.jannotti.billing.core.api.web.controllers.dto.model.customer.CustomerShortDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrdertShortDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.payment.PaymentBankBilletShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDTO extends AbstractModelDTO {

    private String token;
    private CustomerShortDTO customer;
    private String description;
    private String documentNumber;
    private Integer amount;
    private OrdertShortDTO order;
    private String instalment;
    private String creationDate;
    private String expirationDate;
    private String paymentMethod;
    private String status;
    private String paymentDate;
    private String cancelationDate;
    private Integer paidAmount;
    private Integer fees;
    private Integer netAmount;

    private PaymentBankBilletShortDTO bankBillet;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("customer", customer)
            .add("description", description)
            .add("amount", amount)
            .add("order", order)
            .add("instalment", instalment)
            .add("creationDate", creationDate)
            .add("expirationDate", expirationDate)
            .add("paymentMethod", paymentMethod)
            .add("status", status)
            .add("paymentDate", paymentDate)
            .add("cancelationDate", cancelationDate)
            .add("paidAmount", paidAmount)
            .add("fees", fees)
            .add("netAmount", netAmount)
            .add("bankBillet", bankBillet);
    }

}
