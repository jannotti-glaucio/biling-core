package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDTO extends AbstractModelDTO {

    private String token;
    private BankDTO bank;
    private String agencyNumber;
    private String agencyCheckDigit;
    private String accountNumber;
    private String accountCheckDigit;
    private String description;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("bank", bank)
            .add("agencyNumber", agencyNumber)
            .add("agencyCheckDigit", agencyCheckDigit)
            .add("accountNumber", accountNumber)
            .add("accountCheckDigit", accountCheckDigit)
            .add("description", description);
    }

}
