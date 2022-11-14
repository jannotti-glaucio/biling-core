package tech.jannotti.billing.core.commons.enums;

import org.apache.commons.lang.StringUtils;

public enum InvoiceDateToFilterEnum {
    CREATION, EXPIRATION, PAYMENT;

    public static InvoiceDateToFilterEnum valueOfOrNull(String value) {

        if (StringUtils.isBlank(value))
            return null;

        try {
            return InvoiceDateToFilterEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
