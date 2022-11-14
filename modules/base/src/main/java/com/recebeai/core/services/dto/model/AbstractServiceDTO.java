package tech.jannotti.billing.core.services.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractServiceDTO {

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
