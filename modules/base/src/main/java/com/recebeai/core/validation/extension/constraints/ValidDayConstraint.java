package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ValidDay;

public class ValidDayConstraint implements ConstraintValidator<ValidDay, String> {

    @Override
    public void initialize(ValidDay annotation) {
    }

    @Override
    public boolean isValid(String day, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(day))
            return true;

        if (NumberUtils.isNumber(day)) {
            int intDay = Integer.valueOf(day);

            if (intDay < 1 || intDay > 31) {
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

}