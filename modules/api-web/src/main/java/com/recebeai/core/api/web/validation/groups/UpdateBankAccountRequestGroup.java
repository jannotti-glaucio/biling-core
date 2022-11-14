package tech.jannotti.billing.core.api.web.validation.groups;

import tech.jannotti.billing.core.api.web.controllers.dto.request.bank.UpdateBankAccountRestRequest;
import tech.jannotti.billing.core.rest.validation.groups.CRUDOperationGroup;

public class UpdateBankAccountRequestGroup extends CRUDOperationGroup<UpdateBankAccountRestRequest> {

    public UpdateBankAccountRequestGroup() {
        super(UpdateBankAccountRestRequest.class);
    }

}
