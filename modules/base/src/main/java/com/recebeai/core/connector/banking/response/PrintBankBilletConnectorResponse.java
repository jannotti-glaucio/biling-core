package tech.jannotti.billing.core.connector.banking.response;

import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;

@Getter
public class PrintBankBilletConnectorResponse extends ConnectorResponseDTO {

    private byte[] billetContent;

    public PrintBankBilletConnectorResponse(BaseResultCode resultCode) {
        super(resultCode);
    }

    public PrintBankBilletConnectorResponse(BaseResultCode resultCode, byte[] billetContent) {
        super(resultCode);
        this.billetContent = billetContent;
    }

}
