package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum PaymentStatusEnum {
    PENDING("G"),
    DENIED("D"),
    AUTHORIZED("A"),
    CAPTURED("P"),
    CANCELATION_REQUESTED("CR"),
    CANCELED("C"),
    REFUNDED("R"),
    CONTESTED("T"),
    ERROR("E");

    private @Getter String code;

    private PaymentStatusEnum(String code) {
        this.code = code;
    }

    public static PaymentStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (PaymentStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
