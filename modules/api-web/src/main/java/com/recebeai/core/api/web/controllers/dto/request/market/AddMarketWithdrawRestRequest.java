package tech.jannotti.billing.core.api.web.controllers.dto.request.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealerBankAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMarketWithdrawRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @NotBlankParameter
    @ValidDealerBankAccount
    private String bankAccount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("amount", amount)
            .add("bankAccount", bankAccount);
    }

}
