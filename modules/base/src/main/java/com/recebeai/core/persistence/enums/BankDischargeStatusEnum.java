package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankDischargeStatusEnum {
    PENDING("G"), PROCESSED("P"), DENIED("D"), IGNORED("I");

    private @Getter String code;

    private BankDischargeStatusEnum(String code) {
        this.code = code;
    }

    public static BankDischargeStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankDischargeStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
