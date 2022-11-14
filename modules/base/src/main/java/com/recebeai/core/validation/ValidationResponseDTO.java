package tech.jannotti.billing.core.validation;

import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

public class ValidationResponseDTO {

    public BaseResultCode resultCode;
    public String propertyName;
    public String propertyValue;

    public ValidationResponseDTO(BaseResultCode resultCode, String propertyName, String propertyValue) {
        this.resultCode = resultCode;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

}