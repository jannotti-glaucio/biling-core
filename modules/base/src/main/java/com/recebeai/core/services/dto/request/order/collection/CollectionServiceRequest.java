package tech.jannotti.billing.core.services.dto.request.order.collection;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.enums.CollectionAmountTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionServiceRequest extends AbstractServiceRequest {

    private BaseCustomer customer;
    private String description;
    private Long documentNumber;
    private Integer instalments;
    private LocalDate expirationDate;
    private Integer amount;
    private CollectionAmountTypeEnum amountType;
    private PaymentMethodEnum paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("instalments", instalments)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("amountType", amountType)
            .add("paymentMethod", paymentMethod);
    }

}
