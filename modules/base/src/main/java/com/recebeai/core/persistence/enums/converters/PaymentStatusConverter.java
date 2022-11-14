package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(PaymentStatusEnum value) {
        return value.getCode();
    }

    @Override
    public PaymentStatusEnum convertToEntityAttribute(String value) {
        return PaymentStatusEnum.valueOfCode(value);
    }

}
