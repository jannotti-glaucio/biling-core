package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;

@Converter(autoApply = true)
public class BankDischargeStatusConverter implements AttributeConverter<BankDischargeStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankDischargeStatusEnum value) {
        return value.getCode();
    }

    @Override
    public BankDischargeStatusEnum convertToEntityAttribute(String value) {
        return BankDischargeStatusEnum.valueOfCode(value);
    }

}
