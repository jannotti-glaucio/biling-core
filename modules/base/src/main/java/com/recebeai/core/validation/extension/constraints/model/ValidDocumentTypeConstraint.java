package tech.jannotti.billing.core.validation.extension.constraints.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.persistence.repository.base.DocumentTypeRepository;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDocumentType;

public class ValidDocumentTypeConstraint implements ConstraintValidator<ValidDocumentType, String> {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public void initialize(ValidDocumentType annotation) {
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(code))
            return true;

        return documentTypeRepository.existsByCode(code.toUpperCase());
    }

}