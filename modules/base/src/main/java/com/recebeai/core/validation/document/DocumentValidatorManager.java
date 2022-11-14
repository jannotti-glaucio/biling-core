package tech.jannotti.billing.core.validation.document;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.validation.exception.ValidationException;

@Component
public class DocumentValidatorManager {

    @Autowired
    private ApplicationContext context;

    public DocumentValidator getValidator(String validatorId) throws ValidationException {

        Object bean = null;
        try {
            bean = context.getBean(validatorId);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ValidationException("Validador [" + validatorId + "] nao encontrado");
        }

        if (!(bean instanceof DocumentValidator))
            throw new ValidationException("Bean [" + validatorId + "] nao eh do tipo DocumentValidator ["
                + bean.getClass() + "]");

        DocumentValidator documentValidator = (DocumentValidator) bean;
        return documentValidator;
    }

}
