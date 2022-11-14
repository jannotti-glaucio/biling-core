package tech.jannotti.billing.core.validation.extension.annotations.enums;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.validation.extension.constraints.enums.ValidMarketWithdrawStatusListConstraint;

@Constraint(validatedBy = { ValidMarketWithdrawStatusListConstraint.class })
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ValidMarketWithdrawStatusList {

    String message() default ResultCodeConstants.CODE_INVALID_STATUS_LIST_PARAMETER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
