package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankDischargeOperationEnum;

@Converter(autoApply = true)
public class BankDischargeOperationConverter implements AttributeConverter<BankDischargeOperationEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankDischargeOperationEnum value) {
        return value.getCode();
    }

    @Override
    public BankDischargeOperationEnum convertToEntityAttribute(String value) {
        return BankDischargeOperationEnum.valueOfCode(value);
    }

}
