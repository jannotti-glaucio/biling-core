package tech.jannotti.billing.core.api.web.controllers.dto.request.dealer;

import java.util.List;

import javax.validation.Valid;

import tech.jannotti.billing.core.api.web.controllers.dto.request.address.UpdateAddressRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.bank.UpdateBankAccountRestRequest;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

public class UpdateDealerRestRequest extends AbstractDealerRestRequest {

    @Valid
    public List<UpdateAddressRestRequest> addresses;

    @Valid
    public List<UpdateBankAccountRestRequest> bankAccounts;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("addresses", addresses)
            .add("bankAccounts", bankAccounts);
    }
}
