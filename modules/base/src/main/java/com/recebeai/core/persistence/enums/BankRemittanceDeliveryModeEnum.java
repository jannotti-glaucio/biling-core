package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankRemittanceDeliveryModeEnum {
    FILE("F"), WEB_SERVICES("W");

    private @Getter String code;

    private BankRemittanceDeliveryModeEnum(String code) {
        this.code = code;
    }

    public static BankRemittanceDeliveryModeEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankRemittanceDeliveryModeEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
