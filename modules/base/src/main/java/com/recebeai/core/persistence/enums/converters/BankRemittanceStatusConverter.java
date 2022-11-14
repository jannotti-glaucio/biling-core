package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;

@Converter(autoApply = true)
public class BankRemittanceStatusConverter implements AttributeConverter<BankRemittanceStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankRemittanceStatusEnum value) {
        return value.getCode();
    }

    @Override
    public BankRemittanceStatusEnum convertToEntityAttribute(String value) {
        return BankRemittanceStatusEnum.valueOfCode(value);
    }

}
