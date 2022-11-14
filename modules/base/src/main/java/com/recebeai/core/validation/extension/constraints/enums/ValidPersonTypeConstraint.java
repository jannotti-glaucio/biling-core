package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPersonType;

public class ValidPersonTypeConstraint implements ConstraintValidator<ValidPersonType, String> {

    @Override
    public void initialize(ValidPersonType annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            PersonTypeEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}