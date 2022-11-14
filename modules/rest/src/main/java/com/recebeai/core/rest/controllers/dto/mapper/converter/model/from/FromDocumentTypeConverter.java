package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.from;

import org.modelmapper.AbstractConverter;

import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;

public class FromDocumentTypeConverter extends AbstractConverter<BaseDocumentType, String> {

    @Override
    protected String convert(BaseDocumentType source) {
        return source.getCode();
    }

}