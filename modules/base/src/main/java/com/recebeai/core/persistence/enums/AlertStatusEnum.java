package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum AlertStatusEnum {
    PENDING("P"), DELIVERED("D");

    private @Getter String code;

    private AlertStatusEnum(String code) {
        this.code = code;
    }

    public static AlertStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (AlertStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
