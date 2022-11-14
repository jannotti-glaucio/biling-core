package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankRemittanceStatusEnum {
    PENDING("G"), 
    PROCESSED("P"), 
    CANCELLED("C"), 
    ERROR("E");

    private @Getter String code;

    private BankRemittanceStatusEnum(String code) {
        this.code = code;
    }

    public static BankRemittanceStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankRemittanceStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
