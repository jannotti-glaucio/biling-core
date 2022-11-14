package tech.jannotti.billing.core.services.dto.request.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInvoiceFeesServiceRequest extends AbstractServiceRequest {

    private Integer amount;
    private PaymentMethodEnum paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("amount", amount)
            .add("paymentMethod", paymentMethod);
    }

}
