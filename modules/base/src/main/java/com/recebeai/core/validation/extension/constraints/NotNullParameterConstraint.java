package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import tech.jannotti.billing.core.validation.extension.annotations.NotNullParameter;

public class NotNullParameterConstraint implements ConstraintValidator<NotNullParameter, Object> {

    @Override
    public void initialize(NotNullParameter annotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return (value != null);
    }

}
