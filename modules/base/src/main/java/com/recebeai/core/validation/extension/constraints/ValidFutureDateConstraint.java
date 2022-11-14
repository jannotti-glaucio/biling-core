package tech.jannotti.billing.core.validation.extension.constraints;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.validation.extension.annotations.ValidFutureDate;

public class ValidFutureDateConstraint implements ConstraintValidator<ValidFutureDate, String> {

    @Override
    public void initialize(ValidFutureDate annotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(date))
            return true;

        LocalDate localDate = DateTimeHelper.parseFromIsoDate(date);
        LocalDate today = DateTimeHelper.getNowDate();

        return !localDate.isBefore(today);
    }

}
