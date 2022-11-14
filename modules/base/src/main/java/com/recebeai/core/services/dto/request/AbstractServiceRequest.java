package tech.jannotti.billing.core.services.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

public abstract class AbstractServiceRequest {

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
