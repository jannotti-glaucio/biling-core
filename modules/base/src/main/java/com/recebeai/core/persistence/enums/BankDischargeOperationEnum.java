package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankDischargeOperationEnum {
    REGISTRY("R"), PAYMENT("P"), CANCELLATION("C"), PURGE("G"), UNKNOW("U");

    private @Getter String code;

    private BankDischargeOperationEnum(String code) {
        this.code = code;
    }

    public static BankDischargeOperationEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankDischargeOperationEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
