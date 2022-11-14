package tech.jannotti.billing.core.services.dto.request.order.subscription;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubscriptionServiceRequest extends AbstractServiceRequest {

    private String description;
    private Long documentNumber;
    private Integer expirationDay;
    private LocalDate endDate;
    private Integer amount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("endDate", endDate)
            .add("amount", amount);
    }

}
