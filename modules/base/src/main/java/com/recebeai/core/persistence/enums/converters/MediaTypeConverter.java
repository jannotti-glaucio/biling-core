package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;

@Converter(autoApply = true)
public class MediaTypeConverter implements AttributeConverter<MediaTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(MediaTypeEnum value) {
        return value.getCode();
    }

    @Override
    public MediaTypeEnum convertToEntityAttribute(String value) {
        return MediaTypeEnum.valueOfCode(value);
    }

}
