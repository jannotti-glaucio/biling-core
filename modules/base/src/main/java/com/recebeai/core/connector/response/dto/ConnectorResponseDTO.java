package tech.jannotti.billing.core.connector.response.dto;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;

@Getter
public class ConnectorResponseDTO {

    private BaseResultCode resultCode;

    private String[] parameters;

    public ConnectorResponseDTO(BaseResultCode resultCode, String... parameters) {
        this.resultCode = resultCode;
        this.parameters = parameters;
    }

    public boolean isSuccess() {
        return resultCode.isSuccess();
    }

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this)
            .add("resultCode", resultCode);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
