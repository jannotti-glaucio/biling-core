package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum SubscriptionStatusEnum {
    OPEN("O"), SUSPENDED("S"), FINISHED("F"), CANCELED("C");

    private @Getter String code;

    private SubscriptionStatusEnum(String code) {
        this.code = code;
    }

    public static SubscriptionStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (SubscriptionStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

    public static SubscriptionStatusEnum[] valueOfArray(String[] names) {

        if (ArrayUtils.isEmpty(names))
            return null;

        SubscriptionStatusEnum enums[] = new SubscriptionStatusEnum[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            enums[i] = valueOf(name);
        }

        return enums;
    }

}