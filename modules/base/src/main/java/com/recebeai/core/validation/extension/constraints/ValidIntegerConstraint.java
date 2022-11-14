package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;

public class ValidIntegerConstraint implements ConstraintValidator<ValidInteger, String> {

    private boolean positive;

    @Override
    public void initialize(ValidInteger annotation) {
        positive = annotation.positive();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        boolean isNumber = NumberUtils.isNumber(value);

        if (!isNumber)
            return false;

        if (positive) {
            int number = NumberUtils.toInt(value);
            return number >= 0;
        } else
            return isNumber;
    }

}
