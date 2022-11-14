package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.persistence.enums.AddressTypeEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidAddressType;

public class ValidAddressTypeConstraint implements ConstraintValidator<ValidAddressType, String> {

    @Override
    public void initialize(ValidAddressType annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            AddressTypeEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
