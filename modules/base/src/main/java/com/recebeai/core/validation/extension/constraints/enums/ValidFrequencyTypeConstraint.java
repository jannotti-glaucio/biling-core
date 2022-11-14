package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidFrequencyType;

public class ValidFrequencyTypeConstraint implements ConstraintValidator<ValidFrequencyType, String> {

    @Override
    public void initialize(ValidFrequencyType annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            FrequencyTypeEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
