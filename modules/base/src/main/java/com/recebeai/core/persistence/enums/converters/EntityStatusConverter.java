package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;

@Converter(autoApply = true)
public class EntityStatusConverter implements AttributeConverter<EntityStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(EntityStatusEnum value) {
        return value.getCode();
    }

    @Override
    public EntityStatusEnum convertToEntityAttribute(String value) {
        return EntityStatusEnum.valueOfCode(value);
    }

}
