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
public class GetMarketWithdrawFeesRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ValidInteger
    private String amount;

    @NotBlankParameter
    @ValidDealerBankAccount
    // Parametro ainda nao eh usado, mas podemos usar no futuro pra nao cobrar taxa no caso de ser o mesmo banco
    private String bankAccount;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("amount", amount)
            .add("bankAccount", bankAccount);
    }

}
