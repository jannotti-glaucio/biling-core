package tech.jannotti.billing.core.validation.extension.constraints.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.ArrayUtils;

import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidMarketWithdrawStatusList;

public class ValidMarketWithdrawStatusListConstraint implements ConstraintValidator<ValidMarketWithdrawStatusList, String[]> {

    @Override
    public void initialize(ValidMarketWithdrawStatusList annotation) {
    }

    @Override
    public boolean isValid(String values[], ConstraintValidatorContext context) {

        if (ArrayUtils.isEmpty(values))
            return true;

        for (String value : values) {
            try {
                MarketWithdrawStatusEnum.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }

}
