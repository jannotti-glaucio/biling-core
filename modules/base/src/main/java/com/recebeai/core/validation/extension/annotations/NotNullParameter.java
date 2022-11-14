package tech.jannotti.billing.core.validation.extension.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.validation.extension.constraints.NotNullParameterConstraint;

@Constraint(validatedBy = { NotNullParameterConstraint.class })
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface NotNullParameter {

    String message() default ResultCodeConstants.CODE_REQUIRED_PARAMETER_MISSING;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
