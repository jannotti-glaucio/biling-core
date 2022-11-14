package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum OrderTypeEnum {
    COLLECTION("C"), SUBSCRIPTION("S");

    private @Getter String code;

    private OrderTypeEnum(String code) {
        this.code = code;
    }

    public static OrderTypeEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (OrderTypeEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

    public static OrderTypeEnum[] valueOfArray(String[] names) {

        if (ArrayUtils.isEmpty(names))
            return null;

        OrderTypeEnum enums[] = new OrderTypeEnum[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            enums[i] = valueOf(name);
        }

        return enums;
    }

}
