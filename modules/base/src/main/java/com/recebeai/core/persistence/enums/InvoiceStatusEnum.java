package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum InvoiceStatusEnum {
    OPEN("O"), EXPIRED("X"), PAID("P"), CANCELED("C"), ERROR("E");

    private @Getter String code;

    private InvoiceStatusEnum(String code) {
        this.code = code;
    }

    public static InvoiceStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (InvoiceStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

    public static InvoiceStatusEnum[] valueOfArray(String[] names) {

        if (ArrayUtils.isEmpty(names))
            return null;

        InvoiceStatusEnum enums[] = new InvoiceStatusEnum[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            enums[i] = valueOf(name);
        }

        return enums;
    }

}
