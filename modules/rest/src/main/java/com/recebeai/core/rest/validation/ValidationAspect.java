package tech.jannotti.billing.core.rest.validation;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.validation.ValidationManager;
import tech.jannotti.billing.core.validation.ValidationResponseDTO;

@Aspect
@Component
public class ValidationAspect {

    @Autowired
    private ValidationManager validationManager;

    @Pointcut("@annotation(tech.jannotti.billing.core.rest.validation.ValidateBody)")
    public void validateBody() {
    }

    @Before("validateBody()")
    public void validateBody(JoinPoint joinPoint) {

        Object[] params = joinPoint.getArgs();
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            if (param instanceof AbstractRestRequestDTO) {
                ValidationResponseDTO validationResponse = validationManager.validateBean(param);
                if (validationResponse != null)
                    throw new ResultCodeControllerException(validationResponse.resultCode.getKey(),
                        validationResponse.propertyName, validationResponse.propertyValue);
                else
                    break;
            }
        }
    }

    @Pointcut("@annotation(tech.jannotti.billing.core.rest.validation.ValidateParameters)")
    public void validateParameters() {
    }

    @Before("validateParameters()")
    public void validateParameters(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object object = joinPoint.getTarget();
        Method method = signature.getMethod();
        Object[] parameterValues = joinPoint.getArgs();

        ValidationResponseDTO validationResponse = validationManager.validateParameters(object, method, parameterValues);
        if (validationResponse != null)
            throw new ResultCodeControllerException(validationResponse.resultCode.getKey(), validationResponse.propertyName,
                validationResponse.propertyValue);
    }

}