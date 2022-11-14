package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPaymentMethod;

public class ValidPaymentMethodConstraint implements ConstraintValidator<ValidPaymentMethod, String> {

    @Override
    public void initialize(ValidPaymentMethod annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            PaymentMethodEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}