package tech.jannotti.billing.core.constants.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum TransferTypeConstants {
    //@formatter:off
    COMPANY_TO_DEALER_BANK_ACCOUNT("PDBA"), 
    COMPANY_TO_CUSTOMER_BANK_ACCOUNT("PCBA"), 
    DEALER_TO_DEALER_MARKET_ACCOUNT("DDMA"), 
    DEALER_TO_CUSTOMER_MARKET_ACCOUNT("DCMA"), 
    CUSTOMER_TO_DEALER_MARKET_ACCOUNT("CDMA");
    //@formatter:on

    private @Getter String code;

    private TransferTypeConstants(String code) {
        this.code = code;
    }

    public static TransferTypeConstants valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (TransferTypeConstants value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

}
