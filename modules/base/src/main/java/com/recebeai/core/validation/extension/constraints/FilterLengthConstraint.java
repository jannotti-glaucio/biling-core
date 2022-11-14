package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.validation.ValidationConstants;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;

public class FilterLengthConstraint implements ConstraintValidator<FilterLength, String> {

    @Override
    public void initialize(FilterLength annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        return (StringUtils.length(value) >= ValidationConstants.FILTER_MINIMUM_LENGTH);
    }

}
