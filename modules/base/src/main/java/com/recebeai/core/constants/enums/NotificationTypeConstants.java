package tech.jannotti.billing.core.constants.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum NotificationTypeConstants {
    INVOICE_STATUS_UPDATE("ISU");

    private @Getter String code;

    private NotificationTypeConstants(String code) {
        this.code = code;
    }

    public static NotificationTypeConstants valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (NotificationTypeConstants value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
