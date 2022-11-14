package tech.jannotti.billing.core.banking.bb.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.jannotti.billing.core.commons.rest.AbstractRestResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthTokenResponse extends AbstractRestResponseDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Integer expiresIn;

}
