package tech.jannotti.billing.core.services.dto.request.invoice;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceServiceRequest extends AbstractServiceRequest {

    private String description;
    private Long documentNumber;
    private LocalDate expirationDate;
    private Integer amount;
    private LocalDate penaltyStartDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("penaltyStartDate", penaltyStartDate);
    }

}
