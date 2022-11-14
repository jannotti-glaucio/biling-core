package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;

@Converter(autoApply = true)
public class SubscriptionStatusConverter implements AttributeConverter<SubscriptionStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(SubscriptionStatusEnum value) {
        return value.getCode();
    }

    @Override
    public SubscriptionStatusEnum convertToEntityAttribute(String value) {
        return SubscriptionStatusEnum.valueOfCode(value);
    }

}
