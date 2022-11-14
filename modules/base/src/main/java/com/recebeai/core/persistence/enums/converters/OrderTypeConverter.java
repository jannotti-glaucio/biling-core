package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.OrderTypeEnum;

@Converter(autoApply = true)
public class OrderTypeConverter implements AttributeConverter<OrderTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(OrderTypeEnum value) {
        return value.getCode();
    }

    @Override
    public OrderTypeEnum convertToEntityAttribute(String value) {
        return OrderTypeEnum.valueOfCode(value);
    }

}
