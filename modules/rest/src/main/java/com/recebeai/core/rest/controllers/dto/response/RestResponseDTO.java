package tech.jannotti.billing.core.rest.controllers.dto.response;

import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.model.PageDTO;
import tech.jannotti.billing.core.rest.controllers.dto.model.ResultCodeDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestResponseDTO extends AbstractRestResponseDTO {

    public ResultCodeDTO result;

    public PageDTO page;

    public RestResponseDTO(BaseResultCode resultCode, String... parameters) {
        result = new ResultCodeDTO(resultCode, parameters);
    }

    public RestResponseDTO(Page<?> page, BaseResultCode resultCode, String... parameters) {
        this(resultCode, parameters);
        this.page = new PageDTO(page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("result", result)
            .add("page", page);
    }

}
