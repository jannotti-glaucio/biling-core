package tech.jannotti.billing.core.validation.extension.constraints.enums.order;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.ArrayUtils;

import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidSubscriptionStatusList;

public class ValidSubscriptionStatusListConstraint implements ConstraintValidator<ValidSubscriptionStatusList, String[]> {

    @Override
    public void initialize(ValidSubscriptionStatusList annotation) {
    }

    @Override
    public boolean isValid(String values[], ConstraintValidatorContext context) {

        if (ArrayUtils.isEmpty(values))
            return true;

        for (String value : values) {
            try {
                SubscriptionStatusEnum.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

}
