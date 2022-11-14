package tech.jannotti.billing.core.api.web.controllers.dto.model.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketWithdrawShortDTO extends AbstractModelDTO {

    private String token;
    private Integer amount;
    private String requestDate;
    private String status;
    private String releaseDate;
    private Integer fees;
    private Integer netAmount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("amount", amount)
            .add("requestDate", requestDate)
            .add("status", status)
            .add("releaseDate", releaseDate)
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
