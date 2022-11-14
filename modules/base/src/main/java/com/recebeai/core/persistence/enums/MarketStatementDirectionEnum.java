package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum MarketStatementDirectionEnum {
    CREDIT("C"), DEBIT("D");

    private @Getter String code;

    private MarketStatementDirectionEnum(String code) {
        this.code = code;
    }

    public static MarketStatementDirectionEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (MarketStatementDirectionEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
