package tech.jannotti.billing.core.connector.banking.response;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;

@Getter
public class RegisterBankBilletConnectorResponse extends ConnectorResponseDTO {

    private long ourNumber;
    private long yourNumber;
    private String lineCode;

    public RegisterBankBilletConnectorResponse(BaseResultCode resultCode) {
        super(resultCode);
    }

    public RegisterBankBilletConnectorResponse(BaseResultCode resultCode, long ourNumber, long yourNumber, String lineCode) {
        super(resultCode);
        this.ourNumber = ourNumber;
        this.yourNumber = yourNumber;
        this.lineCode = lineCode;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("ourNumber", ourNumber)
            .add("yourNumber", yourNumber)
            .add("lineCode", lineCode);
    }

}
