package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankRemittanceOperationEnum {
    REGISTRY("R"), CANCELLATION("C");

    private @Getter String code;

    private BankRemittanceOperationEnum(String code) {
        this.code = code;
    }

    public static BankRemittanceOperationEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankRemittanceOperationEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
