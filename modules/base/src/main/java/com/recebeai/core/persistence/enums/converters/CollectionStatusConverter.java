package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;

@Converter(autoApply = true)
public class CollectionStatusConverter implements AttributeConverter<CollectionStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(CollectionStatusEnum value) {
        return value.getCode();
    }

    @Override
    public CollectionStatusEnum convertToEntityAttribute(String value) {
        return CollectionStatusEnum.valueOfCode(value);
    }

}
