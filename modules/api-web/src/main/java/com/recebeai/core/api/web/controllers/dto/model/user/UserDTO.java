package tech.jannotti.billing.core.api.web.controllers.dto.model.user;

import tech.jannotti.billing.core.api.web.controllers.dto.model.RoleDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.company.CompanyShortDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.customer.CustomerShortDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.dealer.DealerShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends AbstractModelDTO {

    private String token;
    private String username;
    private String email;
    private String realName;
    private RoleDTO role;
    private String status;

    private CompanyShortDTO company;
    private DealerShortDTO dealer;
    private CustomerShortDTO customer;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("username", username)
            .add("email", email)
            .add("realName", realName)
            .add("role", role)
            .add("status", status)
            .add("company", company)
            .add("dealer", dealer)
            .add("customer", customer);
    }

}