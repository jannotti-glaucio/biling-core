package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;

@Converter(autoApply = true)
public class PersonTypeConverter implements AttributeConverter<PersonTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(PersonTypeEnum value) {
        return value.getCode();
    }

    @Override
    public PersonTypeEnum convertToEntityAttribute(String value) {
        return PersonTypeEnum.valueOfCode(value);
    }

}
