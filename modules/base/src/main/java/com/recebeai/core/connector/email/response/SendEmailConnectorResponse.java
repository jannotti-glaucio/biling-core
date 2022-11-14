package tech.jannotti.billing.core.connector.email.response;

import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;

@Getter
public class SendEmailConnectorResponse extends ConnectorResponseDTO {

    private String messageId;

    public SendEmailConnectorResponse(BaseResultCode resultCode, String messageId) {
        super(resultCode);
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return buildToString()
            .add(messageId, messageId)
            .toString();
    }

}
