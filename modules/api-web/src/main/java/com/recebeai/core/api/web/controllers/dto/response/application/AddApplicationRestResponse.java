package tech.jannotti.billing.core.api.web.controllers.dto.response.application;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddApplicationRestResponse extends RestResponseDTO {

    private String token;
    private String clientId;
    private String clientSecret;

    public AddApplicationRestResponse(BaseResultCode resultCode, String token, String clientId, String clientSecret) {
        super(resultCode);
        this.token = token;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("clientId", clientId)
            .mask("clientSecret", clientSecret);
    }

}