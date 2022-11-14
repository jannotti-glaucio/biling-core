package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ValidMonth;

public class ValidMonthConstraint implements ConstraintValidator<ValidMonth, String> {

    @Override
    public void initialize(ValidMonth annotation) {
    }

    @Override
    public boolean isValid(String month, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(month))
            return true;

        if (NumberUtils.isNumber(month)) {
            int intMonth = Integer.valueOf(month);

            if (intMonth < 1 || intMonth > 12) {
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

}