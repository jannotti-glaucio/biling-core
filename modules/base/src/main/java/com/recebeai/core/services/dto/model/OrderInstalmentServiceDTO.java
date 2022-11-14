package tech.jannotti.billing.core.services.dto.model;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInstalmentServiceDTO extends AbstractServiceDTO {

    private Integer instalment;
    private LocalDate expirationDate;
    private Integer amount;
    private Integer fees;
    private Integer netAmount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("instalment", instalment)
            .add("expirationDate", expirationDate)
            .add("amount", amount)
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
