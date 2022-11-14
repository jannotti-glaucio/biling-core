package tech.jannotti.billing.core.api.web.controllers.dto.model.market;

import tech.jannotti.billing.core.api.web.controllers.dto.model.BankAccountDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.user.UserShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketWithdrawDTO extends AbstractModelDTO {

    private String token;
    private Integer amount;
    private BankAccountDTO bankAccount;
    private String requestDate;
    private UserShortDTO requesterUser;
    private String status;
    private String reviewDate;
    private String denyReason;
    private String cancelationDate;
    private String releaseDate;
    private Integer fees;
    private Integer netAmount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("amount", amount)
            .add("bankAccount", bankAccount)
            .add("requestDate", requestDate)
            .add("requesterUser", requesterUser)
            .add("status", status)
            .add("reviewDate", reviewDate)
            .add("denyReason", denyReason)
            .add("cancelationDate", cancelationDate)
            .add("releaseDate", releaseDate)
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
