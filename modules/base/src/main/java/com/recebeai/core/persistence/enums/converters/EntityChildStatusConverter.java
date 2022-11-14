package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;

@Converter(autoApply = true)
public class EntityChildStatusConverter implements AttributeConverter<EntityChildStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(EntityChildStatusEnum value) {
        return value.getCode();
    }

    @Override
    public EntityChildStatusEnum convertToEntityAttribute(String value) {
        return EntityChildStatusEnum.valueOfCode(value);
    }

}
