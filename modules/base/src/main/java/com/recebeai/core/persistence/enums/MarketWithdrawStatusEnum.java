package tech.jannotti.billing.core.persistence.enums;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.exception.EnumNotFoundException;

import lombok.Getter;

public enum MarketWithdrawStatusEnum {
    REQUESTED("R"), APPROVED("A"), DENIED("D"), RELEASED("L"), CANCELED("C");

    private @Getter String code;

    private MarketWithdrawStatusEnum(String code) {
        this.code = code;
    }

    public static MarketWithdrawStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (MarketWithdrawStatusEnum value : values()) {
            if (value.code.equals(code))
                return value;
        }
        throw new EnumNotFoundException(code);
    }

    public static MarketWithdrawStatusEnum[] valueOfArray(String[] names) {

        if (ArrayUtils.isEmpty(names))
            return null;

        MarketWithdrawStatusEnum enums[] = new MarketWithdrawStatusEnum[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            enums[i] = valueOf(name);
        }

        return enums;
    }

}
