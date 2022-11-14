package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.NotificationStatusEnum;

@Converter(autoApply = true)
public class NotificationStatusConverter implements AttributeConverter<NotificationStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(NotificationStatusEnum value) {
        return value.getCode();
    }

    @Override
    public NotificationStatusEnum convertToEntityAttribute(String value) {
        return NotificationStatusEnum.valueOfCode(value);
    }

}
