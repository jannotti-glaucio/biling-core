package tech.jannotti.billing.core.validation.extension.constraints;

import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;

public class ValidDateConstraint implements ConstraintValidator<ValidDate, String> {

    @Override
    public void initialize(ValidDate annotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(date))
            return true;

        try {
            DateTimeHelper.parseFromIsoDate(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}