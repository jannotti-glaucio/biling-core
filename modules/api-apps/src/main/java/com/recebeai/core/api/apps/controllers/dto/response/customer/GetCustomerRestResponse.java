package tech.jannotti.billing.core.api.apps.controllers.dto.response.customer;

import tech.jannotti.billing.core.api.apps.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.apps.controllers.dto.model.CustomerDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCustomerRestResponse extends RestResponseDTO {

    private CustomerDTO customer;

    public GetCustomerRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseCustomer customer) {
        super(resultCode);
        this.customer = dtoMapper.map(customer, CustomerDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("customer", customer);
    }

}