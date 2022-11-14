package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidCRUDOperation;

public class ValidCRUDOperationConstraint implements ConstraintValidator<ValidCRUDOperation, String> {

    @Override
    public void initialize(ValidCRUDOperation annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            CRUDOperationEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}