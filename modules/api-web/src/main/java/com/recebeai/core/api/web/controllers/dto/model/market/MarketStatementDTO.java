package tech.jannotti.billing.core.api.web.controllers.dto.model.market;

import tech.jannotti.billing.core.api.web.controllers.dto.model.payment.PaymentShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketStatementDTO extends AbstractModelDTO {

    private String token;
    private MarketStatementTypeDTO type;
    private String statementDate;
    private Integer amount;
    private Integer balance;
    private PaymentShortDTO payment;
    private MarketWithdrawShortDTO withdraw;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("type", type)
            .add("statementDate", statementDate)
            .add("amount", amount)
            .add("balance", balance)
            .add("payment", payment)
            .add("withdraw", withdraw);
    }

}
