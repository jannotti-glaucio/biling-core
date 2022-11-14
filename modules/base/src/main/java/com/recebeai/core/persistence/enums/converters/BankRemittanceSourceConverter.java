package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankRemittanceSourceEnum;

@Converter(autoApply = true)
public class BankRemittanceSourceConverter implements AttributeConverter<BankRemittanceSourceEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankRemittanceSourceEnum value) {
        return value.getCode();
    }

    @Override
    public BankRemittanceSourceEnum convertToEntityAttribute(String value) {
        return BankRemittanceSourceEnum.valueOfCode(value);
    }

}
