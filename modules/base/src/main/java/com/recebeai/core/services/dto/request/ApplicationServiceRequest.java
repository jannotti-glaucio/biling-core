package tech.jannotti.billing.core.services.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationServiceRequest extends AbstractServiceRequest {

    private String name;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("name", name);
    }

}