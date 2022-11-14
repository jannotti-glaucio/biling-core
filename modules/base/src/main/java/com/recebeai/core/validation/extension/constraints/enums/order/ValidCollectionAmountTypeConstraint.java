package tech.jannotti.billing.core.validation.extension.constraints.enums.order;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.enums.CollectionAmountTypeEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidCollectionAmountType;

public class ValidCollectionAmountTypeConstraint implements ConstraintValidator<ValidCollectionAmountType, String> {

    @Override
    public void initialize(ValidCollectionAmountType annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            CollectionAmountTypeEnum.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}