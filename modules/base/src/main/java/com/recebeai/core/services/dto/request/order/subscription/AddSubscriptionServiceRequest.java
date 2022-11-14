package tech.jannotti.billing.core.services.dto.request.order.subscription;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSubscriptionServiceRequest extends AbstractServiceRequest {

    private BaseCustomer customer;
    private String description;
    private Long documentNumber;
    private FrequencyTypeEnum frequencyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amount;
    private PaymentMethodEnum paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("frequencyType", frequencyType)
            .add("startDate", startDate)
            .add("endDate", endDate)
            .add("amount", amount)
            .add("paymentMethod", paymentMethod);
    }

}
