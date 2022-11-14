package tech.jannotti.billing.core.api.web.controllers.dto.request.dealer;

import java.util.List;

import javax.validation.Valid;

import tech.jannotti.billing.core.api.web.controllers.dto.request.address.AddressRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.bank.BankAccountRestRequest;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDealerRestRequest extends AbstractDealerRestRequest {

    @Valid
    private List<AddressRestRequest> addresses;

    @Valid
    private List<BankAccountRestRequest> bankAccounts;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("addresses", addresses)
            .add("bankAccounts", bankAccounts);
    }

}
