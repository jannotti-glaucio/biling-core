package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum PaymentMethodEnum {
    BANK_BILLET("B"), CREDIT_CARD("C"), DEBIT_CARD("D");

    private @Getter String code;

    private PaymentMethodEnum(String code) {
        this.code = code;
    }

    public static PaymentMethodEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (PaymentMethodEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}