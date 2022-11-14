package tech.jannotti.billing.core.services.dto.request.bank;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBankAccountServiceRequest extends BankAccountServiceRequest {

    private CRUDOperationEnum operation;
    private String token;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token);
    }

}
