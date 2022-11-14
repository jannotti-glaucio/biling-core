package tech.jannotti.billing.core.api.apps.controllers.dto.model.invoice;

import tech.jannotti.billing.core.api.apps.controllers.dto.model.CustomerDTO;
import tech.jannotti.billing.core.api.apps.controllers.dto.model.PaymentBankBilletDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDTO extends AbstractModelDTO {

    private String token;
    private CustomerDTO customer;
    private String description;
    private String documentNumber;
    private String amount;
    private String instalment;
    private String expirationDate;
    private String paymentMethod;
    private String status;
    private String paymentDate;
    private String cancelationDate;
    private Integer paidAmount;
    private Integer fees;
    private Integer netAmount;

    private PaymentBankBilletDTO bankBillet;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("amount", amount)
            .add("instalment", instalment)
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
