package tech.jannotti.billing.core.validation.extension.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort.Direction;

import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;

public class ValidSortDirectionConstraint implements ConstraintValidator<ValidSortDirection, String> {

    @Override
    public void initialize(ValidSortDirection annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return true;

        try {
            Direction.fromString(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
