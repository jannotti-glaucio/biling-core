package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.type;

import java.time.LocalDateTime;

import org.modelmapper.AbstractConverter;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;

public class ToLocalDateTimeConverter extends AbstractConverter<String, LocalDateTime> {

    @Override
    protected LocalDateTime convert(String source) {
        return DateTimeHelper.parseFromIsoTimestamp(source);
    }

}
