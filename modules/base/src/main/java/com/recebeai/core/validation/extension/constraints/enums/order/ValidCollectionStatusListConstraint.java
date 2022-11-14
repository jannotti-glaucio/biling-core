package tech.jannotti.billing.core.validation.extension.constraints.enums.order;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.ArrayUtils;

import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidCollectionStatusList;

public class ValidCollectionStatusListConstraint implements ConstraintValidator<ValidCollectionStatusList, String[]> {

    @Override
    public void initialize(ValidCollectionStatusList annotation) {
    }

    @Override
    public boolean isValid(String values[], ConstraintValidatorContext context) {

        if (ArrayUtils.isEmpty(values))
            return true;

        for (String value : values) {
            try {
                CollectionStatusEnum.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

}
