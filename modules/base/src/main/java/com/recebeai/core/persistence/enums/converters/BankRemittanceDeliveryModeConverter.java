package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.BankRemittanceDeliveryModeEnum;

@Converter(autoApply = true)
public class BankRemittanceDeliveryModeConverter implements AttributeConverter<BankRemittanceDeliveryModeEnum, String> {

    @Override
    public String convertToDatabaseColumn(BankRemittanceDeliveryModeEnum value) {
        return value.getCode();
    }

    @Override
    public BankRemittanceDeliveryModeEnum convertToEntityAttribute(String value) {
        return BankRemittanceDeliveryModeEnum.valueOfCode(value);
    }

}
