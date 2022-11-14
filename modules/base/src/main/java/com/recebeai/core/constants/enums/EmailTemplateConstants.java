package tech.jannotti.billing.core.constants.enums;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum EmailTemplateConstants {
    //@formatter:off    
    BANK_BILLET_INVOICE_SEND("bank-billet-invoice-send"),
    BANK_BILLET_COLLECTION_INVOICE_EXPIRING("bank-billet-collection-invoice-expiring"),  
    MARKET_WITHDRAW_REQUEST("market-withdraw-request"),
    MARKET_WITHDRAW_DENY("market-withdraw-deny"),
    MARKET_WITHDRAW_RELEASE("market-withdraw-release");
    //@formatter:on

    private @Getter String name;

    private EmailTemplateConstants(String name) {
        this.name = name;
    }

    public static EmailTemplateConstants valueOfCode(String name) {
        if (StringUtils.isBlank(name))
            return null;

        for (EmailTemplateConstants value : values()) {
            if (value.name.equals(name))
                return value;
        }
        throw new EnumNotFoundException(name);
    }

}
