package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum NotificationStatusEnum {
    PENDING("P"), DELIVERED("D"), UNDELIVERED("U"), IGNORE("I");

    private @Getter String code;

    private NotificationStatusEnum(String code) {
        this.code = code;
    }

    public static NotificationStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (NotificationStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
