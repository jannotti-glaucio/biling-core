package tech.jannotti.billing.core.services.dto.response;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

public abstract class AbstractServiceResponse {

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
