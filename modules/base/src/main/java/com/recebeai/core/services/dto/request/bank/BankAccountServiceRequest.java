package tech.jannotti.billing.core.services.dto.request.bank;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.services.dto.request.AbstractServiceRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountServiceRequest extends AbstractServiceRequest {

    private BaseBank bank;
    private String agencyNumber;
    private String agencyCheckDigit;
    private String accountNumber;
    private String accountCheckDigit;
    private String description;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("bank", bank)
            .add("agencyNumber", agencyNumber)
            .add("agencyCheckDigit", agencyCheckDigit)
            .add("accountNumber", accountNumber)
            .add("accountCheckDigit", accountCheckDigit)
            .add("description", description);
    }
}
