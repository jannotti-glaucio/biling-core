package tech.jannotti.billing.core.api.web.controllers.dto.model.order;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCollectionInstalmentDTO extends AbstractModelDTO {

    private Integer instalment;
    private String expirationDate;
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
