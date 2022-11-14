package tech.jannotti.billing.core.rest.controllers.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;

public class PageRestRequest extends AbstractRestRequestDTO {

    public Integer page;
    public Integer size;
    public String sort;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("page", page)
            .add("size", size)
            .add("sort", sort);
    }
}
