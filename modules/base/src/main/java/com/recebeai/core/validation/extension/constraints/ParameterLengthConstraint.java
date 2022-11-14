package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;

public class ParameterLengthConstraint implements ConstraintValidator<ParameterLength, String> {

    private int max;

    @Override
    public void initialize(ParameterLength annotation) {
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value))
            return true;

        return (StringUtils.length(value) <= max);
    }

}
