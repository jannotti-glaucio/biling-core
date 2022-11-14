package tech.jannotti.billing.core.api.web.controllers.dto.request.user;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDealerUserRestRequest extends AddUserRestRequest {

    @NotBlankParameter
    @ValidDealer
    private String dealer;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("dealer", dealer);
    }

}