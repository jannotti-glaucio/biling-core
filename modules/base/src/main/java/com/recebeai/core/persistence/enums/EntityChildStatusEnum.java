package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum EntityChildStatusEnum {
    ACTIVE("A"), DELETED("D");

    private @Getter String code;

    private EntityChildStatusEnum(String code) {
        this.code = code;
    }

    public static EntityChildStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (EntityChildStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
