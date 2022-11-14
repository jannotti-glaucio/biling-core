package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethodEnum, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethodEnum value) {
        return value.getCode();
    }

    @Override
    public PaymentMethodEnum convertToEntityAttribute(String value) {
        return PaymentMethodEnum.valueOfCode(value);
    }

}
