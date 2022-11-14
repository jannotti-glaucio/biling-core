package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;

@Converter(autoApply = true)
public class BankRemittanceOperationConverter implements AttributeConverter<BankRemittanceOperationEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankRemittanceOperationEnum value) {
        return value.getCode();
    }

    @Override
    public BankRemittanceOperationEnum convertToEntityAttribute(String value) {
        return BankRemittanceOperationEnum.valueOfCode(value);
    }

}
