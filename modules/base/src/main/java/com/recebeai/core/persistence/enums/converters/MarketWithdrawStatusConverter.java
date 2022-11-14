package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;

@Converter(autoApply = true)
public class MarketWithdrawStatusConverter implements AttributeConverter<MarketWithdrawStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(MarketWithdrawStatusEnum value) {
        return value.getCode();
    }

    @Override
    public MarketWithdrawStatusEnum convertToEntityAttribute(String value) {
        return MarketWithdrawStatusEnum.valueOfCode(value);
    }

}
