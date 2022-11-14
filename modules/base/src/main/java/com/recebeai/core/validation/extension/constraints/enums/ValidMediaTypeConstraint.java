package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidMediaType;

public class ValidMediaTypeConstraint implements ConstraintValidator<ValidMediaType, String> {

    @Override
    public void initialize(ValidMediaType annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            MediaTypeEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
