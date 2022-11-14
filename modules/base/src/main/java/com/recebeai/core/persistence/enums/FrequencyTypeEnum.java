package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum FrequencyTypeEnum {
    MONTHLY("M");

    private @Getter String code;

    private FrequencyTypeEnum(String code) {
        this.code = code;
    }

    public static FrequencyTypeEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (FrequencyTypeEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

    public static FrequencyTypeEnum[] valueOfArray(String[] names) {

        if (ArrayUtils.isEmpty(names))
            return null;

        FrequencyTypeEnum enums[] = new FrequencyTypeEnum[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            enums[i] = valueOf(name);
        }

        return enums;
    }

}