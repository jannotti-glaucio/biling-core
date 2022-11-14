package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.AddressTypeEnum;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(AddressTypeEnum value) {
        return value.getCode();
    }

    @Override
    public AddressTypeEnum convertToEntityAttribute(String value) {
        return AddressTypeEnum.valueOfCode(value);
    }

}
