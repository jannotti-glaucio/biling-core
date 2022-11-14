package tech.jannotti.billing.core.validation;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.services.ResultCodeService;

@Component
public class ValidationManager {

    private static final LogManager logManager = LogFactory.getManager(ValidationManager.class);

    @Autowired
    private ResultCodeService resultCodeService;

    @Autowired
    protected Validator validator;

    private ExecutableValidator executableValidator;

    @PostConstruct
    public void init() {
        executableValidator = validator.unwrap(Validator.class).forExecutables();
    }

    private <T> ValidationResponseDTO convertToViolationDTO(Set<ConstraintViolation<T>> violations) {
        ValidationResponseDTO validationResponse = null;

        for (ConstraintViolation<T> constraintViolation : violations) {
            logManager.logDEBUG("Retornando constraint de validacao [" + constraintViolation + "]");

            BaseResultCode resultCode = resultCodeService.getByKey(constraintViolation.getMessage());
            if (resultCode == null) {
                logManager.logERROR("Erro tentando converter contraint [%s] para ResultCode",
                    constraintViolation.getMessage());
                resultCode = resultCodeService.getGenericErrorResultCode();
            }

            // Pega o ultimo no do path da propriedade
            Iterator<Path.Node> pathIterator = constraintViolation.getPropertyPath().iterator();
            Path.Node pathNode = null;
            while (pathIterator.hasNext()) {
                pathNode = pathIterator.next();
            }

            String propertyName = pathNode.getName();
            String propertyValue = (constraintViolation.getInvalidValue() != null
                ? constraintViolation.getInvalidValue().toString()
                : null);

            validationResponse = new ValidationResponseDTO(resultCode, propertyName, propertyValue);
            break;
        }
        return validationResponse;
    }

    public <T> ValidationResponseDTO validateBean(T request) {

        Set<ConstraintViolation<T>> violations = validator.validate(request);

        ValidationResponseDTO validationResponse = convertToViolationDTO(violations);
        return validationResponse;
    }

    public <T> ValidationResponseDTO validateParameters(T object, Method method, Object[] parameterValues) {

        Set<ConstraintViolation<T>> violations = executableValidator.validateParameters(object, method,
            parameterValues);

        ValidationResponseDTO validationResponse = convertToViolationDTO(violations);
        return validationResponse;
    }

}
