package tech.jannotti.billing.core.services.dto.request.invoice;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrder;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInvoiceServiceRequest extends AbstractServiceRequest {

    private BaseOrder order;
    private BaseCustomer customer;
    private String description;
    private Long documentNumber;
    private LocalDate referenceDate;
    private LocalDate expirationDate;
    private Integer amount;
    private LocalDate penaltyStartDate;
    private String callbackUrl;
    private PaymentMethodEnum paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("order", order)
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("referenceDate", referenceDate)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("penaltyStartDate", penaltyStartDate)
            .add("callbackUrl", callbackUrl)
            .add("paymentMethod", paymentMethod);
    }

}
