package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.MarketStatementDirectionEnum;

@Converter(autoApply = true)
public class MarketStatementDirectionConverter implements AttributeConverter<MarketStatementDirectionEnum, String> {

    @Override
    public String convertToDatabaseColumn(MarketStatementDirectionEnum value) {
        return value.getCode();
    }

    @Override
    public MarketStatementDirectionEnum convertToEntityAttribute(String value) {
        return MarketStatementDirectionEnum.valueOfCode(value);
    }

}
