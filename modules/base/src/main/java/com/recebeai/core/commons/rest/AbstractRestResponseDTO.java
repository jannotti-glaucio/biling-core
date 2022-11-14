package tech.jannotti.billing.core.commons.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractRestResponseDTO {

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
