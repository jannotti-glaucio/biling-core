package tech.jannotti.billing.core.api.web.validation.groups;

import tech.jannotti.billing.core.api.web.controllers.dto.request.address.UpdateAddressRestRequest;
import tech.jannotti.billing.core.rest.validation.groups.CRUDOperationGroup;

public class UpdateAddressRequestGroup extends CRUDOperationGroup<UpdateAddressRestRequest> {

    public UpdateAddressRequestGroup() {
        super(UpdateAddressRestRequest.class);
    }

}
