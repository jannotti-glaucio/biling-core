package tech.jannotti.billing.core.constants.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum MarketStatamentTypeConstants {
    //@formatter:off
    INVOICE_PAYMENT("001"),
    WITHDRAW_RESERVE("002"),
    WITHDRAW_REFUND("003");
    //@formatter:on

    private @Getter String code;

    private MarketStatamentTypeConstants(String code) {
        this.code = code;
    }

    public static MarketStatamentTypeConstants valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (MarketStatamentTypeConstants value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
