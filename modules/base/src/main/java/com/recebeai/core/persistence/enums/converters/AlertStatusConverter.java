package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.AlertStatusEnum;

@Converter(autoApply = true)
public class AlertStatusConverter implements AttributeConverter<AlertStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(AlertStatusEnum value) {
        return value.getCode();
    }

    @Override
    public AlertStatusEnum convertToEntityAttribute(String value) {
        return AlertStatusEnum.valueOfCode(value);
    }

}
