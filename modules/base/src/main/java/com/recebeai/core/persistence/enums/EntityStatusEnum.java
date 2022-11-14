package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum EntityStatusEnum {
    ACTIVE("A"), BLOCKED("B"), DELETED("D");

    private @Getter String code;

    private EntityStatusEnum(String code) {
        this.code = code;
    }

    public static EntityStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (EntityStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
