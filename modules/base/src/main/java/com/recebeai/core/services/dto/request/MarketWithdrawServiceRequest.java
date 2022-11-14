package tech.jannotti.billing.core.services.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketWithdrawServiceRequest extends AbstractServiceRequest {

    private Integer amount;
    private BaseDealerBankAccount bankAccount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("amount", amount)
            .add("bankAccount", bankAccount);
    }

}
