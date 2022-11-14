package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ValidYear;

public class ValidYearConstraint implements ConstraintValidator<ValidYear, String> {

    @Override
    public void initialize(ValidYear annotation) {
    }

    @Override
    public boolean isValid(String year, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(year))
            return true;

        if (NumberUtils.isNumber(year)) {
            if (year.length() < 4 || year.length() > 4) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}