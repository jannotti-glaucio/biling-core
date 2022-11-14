package tech.jannotti.billing.core.persistence.enums.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;

@Converter(autoApply = true)
public class InvoiceStatusConverter implements AttributeConverter<InvoiceStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(InvoiceStatusEnum value) {
        return value.getCode();
    }

    @Override
    public InvoiceStatusEnum convertToEntityAttribute(String value) {
        return InvoiceStatusEnum.valueOfCode(value);
    }

}
