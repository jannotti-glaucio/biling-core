package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.type;

import java.time.LocalDate;

import org.modelmapper.AbstractConverter;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;

public class ToLocalDateConverter extends AbstractConverter<String, LocalDate> {

    @Override
    protected LocalDate convert(String source) {
        return DateTimeHelper.parseFromIsoDate(source);
    }

}
