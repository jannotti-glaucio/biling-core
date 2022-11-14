package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceDateToFilter;

public class ValidInvoiceDateToFilterConstraint implements ConstraintValidator<ValidInvoiceDateToFilter, String> {

    @Override
    public void initialize(ValidInvoiceDateToFilter annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            InvoiceDateToFilterEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
