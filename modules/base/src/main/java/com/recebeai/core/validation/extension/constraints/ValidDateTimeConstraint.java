package tech.jannotti.billing.core.validation.extension.constraints;

import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDateTime;

public class ValidDateTimeConstraint implements ConstraintValidator<ValidDateTime, String> {

    @Override
    public void initialize(ValidDateTime annotation) {
    }

    @Override
    public boolean isValid(String dateTime, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(dateTime))
            return true;

        try {
            DateTimeHelper.parseFromIsoTimestamp(dateTime);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}