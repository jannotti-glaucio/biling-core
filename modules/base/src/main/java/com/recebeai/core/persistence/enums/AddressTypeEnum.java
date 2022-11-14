package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum AddressTypeEnum {
    HOME("H"), BUSINESS("B"), MAILING("M");

    private @Getter String code;

    private AddressTypeEnum(String code) {
        this.code = code;
    }

    public static AddressTypeEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (AddressTypeEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
