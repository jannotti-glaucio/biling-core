package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;

@Converter(autoApply = true)
public class FrequencyTypeConverter implements AttributeConverter<FrequencyTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(FrequencyTypeEnum value) {
        return value.getCode();
    }

    @Override
    public FrequencyTypeEnum convertToEntityAttribute(String value) {
        return FrequencyTypeEnum.valueOfCode(value);
    }

}
