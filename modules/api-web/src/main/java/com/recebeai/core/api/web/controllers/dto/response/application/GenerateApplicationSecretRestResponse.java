package tech.jannotti.billing.core.api.web.controllers.dto.response.application;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenerateApplicationSecretRestResponse extends RestResponseDTO {

    private String clientId;
    private String clientSecret;

    public GenerateApplicationSecretRestResponse(BaseResultCode resultCode, String clientId, String clientSecret) {
        super(resultCode);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("clientId", clientId)
            .mask("clientSecret", clientSecret);
    }

}