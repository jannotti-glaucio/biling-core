package tech.jannotti.billing.core.api.web.controllers.dto.model.company;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyShortDTO extends AbstractModelDTO {

    private String token;
    private String tradingName;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("tradingName", tradingName);
    }

}