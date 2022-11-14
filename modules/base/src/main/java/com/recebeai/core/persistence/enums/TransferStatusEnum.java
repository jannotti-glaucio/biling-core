package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum TransferStatusEnum {
    PENDING("G"), DONE("D"), CANCELED("C");

    private @Getter String code;

    private TransferStatusEnum(String code) {
        this.code = code;
    }

    public static TransferStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (TransferStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
