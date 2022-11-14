package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;

public class NotBlankParameterConstraint implements ConstraintValidator<NotBlankParameter, String> {

    @Override
    public void initialize(NotBlankParameter annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value);
    }

}
