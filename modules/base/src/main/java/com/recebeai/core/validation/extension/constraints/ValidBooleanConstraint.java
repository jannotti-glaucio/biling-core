package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.validation.extension.annotations.ValidBoolean;

public class ValidBooleanConstraint implements ConstraintValidator<ValidBoolean, String> {

    @Override
    public void initialize(ValidBoolean annotation) {
    }

    @Override
    public boolean isValid(String bool, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(bool))
            return true;

        if (!bool.equals("true") && !bool.equals("false"))
            return true;

        return true;
    }

}
