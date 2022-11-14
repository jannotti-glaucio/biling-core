package tech.jannotti.billing.core.validation.extension.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.validation.extension.constraints.ParameterLengthConstraint;

@Constraint(validatedBy = { ParameterLengthConstraint.class })
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ParameterLength {

    int max() default 0;

    String message() default ResultCodeConstants.CODE_LENGTH_PARAMETER_EXCEEDS_LMIT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
