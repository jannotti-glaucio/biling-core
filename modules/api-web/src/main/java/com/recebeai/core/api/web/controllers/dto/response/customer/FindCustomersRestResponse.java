package tech.jannotti.billing.core.api.web.controllers.dto.response.customer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.customer.CustomerDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindCustomersRestResponse extends RestResponseDTO {

    private List<CustomerDTO> customers;

    // TODO Ver uma forma do DTO ter acesso direto ao beanMapper, sem precisar passar pelo construtor
    public FindCustomersRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseCustomer> page) {
        super(page, resultCode);
        this.customers = dtoMapper.mapList(page.getContent(), CustomerDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customers.size", CollectionUtils.size(customers));
    }

}