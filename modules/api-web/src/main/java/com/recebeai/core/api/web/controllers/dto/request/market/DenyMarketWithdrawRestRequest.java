package tech.jannotti.billing.core.api.web.controllers.dto.request.market;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DenyMarketWithdrawRestRequest extends AbstractRestRequestDTO {

    @ParameterLength(max = 100)
    private String denyReason;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("denyReason", denyReason);
    }

}
