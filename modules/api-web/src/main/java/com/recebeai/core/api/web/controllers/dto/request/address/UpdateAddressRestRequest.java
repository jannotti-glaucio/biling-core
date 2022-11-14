package tech.jannotti.billing.core.api.web.controllers.dto.request.address;

import org.hibernate.validator.group.GroupSequenceProvider;

import tech.jannotti.billing.core.api.web.validation.groups.UpdateAddressRequestGroup;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.request.CRUDOperationRestRequest;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidCRUDOperation;
import tech.jannotti.billing.core.validation.extension.groups.DeleteValidations;
import tech.jannotti.billing.core.validation.extension.groups.UpdateValidations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupSequenceProvider(UpdateAddressRequestGroup.class)
public class UpdateAddressRestRequest extends AddressRestRequest implements CRUDOperationRestRequest {

    @NotBlankParameter
    @ValidCRUDOperation
    private String operation;

    @NotBlankParameter(groups = { UpdateValidations.class, DeleteValidations.class })
    private String token;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("operation", operation)
            .add("token", token);
    }

}
