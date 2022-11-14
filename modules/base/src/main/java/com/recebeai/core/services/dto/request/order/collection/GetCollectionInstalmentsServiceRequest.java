package tech.jannotti.billing.core.services.dto.request.order.collection;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.enums.CollectionAmountTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCollectionInstalmentsServiceRequest extends AbstractServiceRequest {

    private Integer instalments;
    private LocalDate expirationDate;
    private Integer amount;
    private CollectionAmountTypeEnum amountType;
    private PaymentMethodEnum paymentMethod;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("expirationDate", expirationDate)
            .add("instalments", instalments)
            .add("amount", amount)
            .add("amountType", amountType)
            .add("paymentMethod", paymentMethod);
    }

}
