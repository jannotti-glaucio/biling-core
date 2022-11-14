package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum BankRemittanceSourceEnum {
    BANK_BILLET("B"), TRANSFER("T");

    private @Getter String code;

    private BankRemittanceSourceEnum(String code) {
        this.code = code;
    }

    public static BankRemittanceSourceEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (BankRemittanceSourceEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
