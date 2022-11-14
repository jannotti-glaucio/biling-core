package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.TransferStatusEnum;

@Converter(autoApply = true)
public class TransferStatusConverter implements AttributeConverter<TransferStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(TransferStatusEnum value) {
        return value.getCode();
    }

    @Override
    public TransferStatusEnum convertToEntityAttribute(String value) {
        return TransferStatusEnum.valueOfCode(value);
    }

}
