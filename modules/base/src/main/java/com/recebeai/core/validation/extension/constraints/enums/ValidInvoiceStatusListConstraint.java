package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.ArrayUtils;

import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceStatusList;

public class ValidInvoiceStatusListConstraint implements ConstraintValidator<ValidInvoiceStatusList, String[]> {

    @Override
    public void initialize(ValidInvoiceStatusList annotation) {
    }

    @Override
    public boolean isValid(String values[], ConstraintValidatorContext context) {

        if (ArrayUtils.isEmpty(values))
            return true;

        for (String value : values) {
            try {
                InvoiceStatusEnum.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

}
