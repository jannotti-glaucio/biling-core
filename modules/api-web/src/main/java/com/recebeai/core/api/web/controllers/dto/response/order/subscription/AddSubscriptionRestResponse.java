package tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class AddSubscriptionRestResponse extends RestResponseDTO {

    private String token;

    public AddSubscriptionRestResponse(BaseResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token);
    }

}