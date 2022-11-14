package tech.jannotti.billing.core.constants.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum AlertTypeConstants {
    //@formatter:off    
    INVOICE_SEND("IS"), 
    COLLECTION_INVOICE_EXPIRING("CIE"),
    MARKET_WITHDRAW_REQUEST("MWR"), 
    MARKET_WITHDRAW_DENY("MWD"),
    MARKET_WITHDRAW_RELEASE("MWL");
    //@formatter:on

    private @Getter String code;

    private AlertTypeConstants(String code) {
        this.code = code;
    }

    public static AlertTypeConstants valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (AlertTypeConstants value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
