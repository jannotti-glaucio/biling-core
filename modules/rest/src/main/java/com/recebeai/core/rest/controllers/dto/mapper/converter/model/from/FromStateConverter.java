package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.from;

import org.modelmapper.AbstractConverter;

import tech.jannotti.billing.core.persistence.model.base.BaseState;

public class FromStateConverter extends AbstractConverter<BaseState, String> {

    @Override
    protected String convert(BaseState source) {
        return source.getCode();
    }

}