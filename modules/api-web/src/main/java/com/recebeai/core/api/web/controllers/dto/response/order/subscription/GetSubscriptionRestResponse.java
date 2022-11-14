package tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrderSubscriptionDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class GetSubscriptionRestResponse extends RestResponseDTO {

    private OrderSubscriptionDTO subscription;

    public GetSubscriptionRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseOrderSubscription subscription) {
        super(resultCode);
        this.subscription = dtoMapper.map(subscription, OrderSubscriptionDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("subscription", subscription);
    }

}