package tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.order.OrderSubscriptionDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class FindSubscriptionsRestResponse extends RestResponseDTO {

    private List<OrderSubscriptionDTO> subscriptions;

    public FindSubscriptionsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseOrderSubscription> page) {
        super(page, resultCode);
        this.subscriptions = dtoMapper.mapList(page.getContent(), OrderSubscriptionDTO.class);
    }

    public FindSubscriptionsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<BaseOrderSubscription> subscriptions) {
        super(resultCode);
        this.subscriptions = dtoMapper.mapList(subscriptions, OrderSubscriptionDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("subscriptions.size", CollectionUtils.size(subscriptions));
    }

}