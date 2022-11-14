package tech.jannotti.billing.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.repository.base.DocumentTypeRepository;
import tech.jannotti.billing.core.validation.document.DocumentValidator;
import tech.jannotti.billing.core.validation.document.DocumentValidatorManager;

@Service
public class DocumentTypeService extends AbstractService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentValidatorManager documentValidatorManager;

    public List<BaseDocumentType> list(BaseCountry country) {
        return documentTypeRepository.findByCountry(country);
    }

    public BaseDocumentType get(String code) {
        return documentTypeRepository.getByCode(code);
    }

    public BaseDocumentType get(BaseCountry country, PersonTypeEnum personType) {
        return documentTypeRepository.getByCountryAndPersonType(country, personType);
    }

    public boolean validate(BaseDocumentType documentType, String documentNumber) {

        DocumentValidator validator = documentValidatorManager.getValidator(documentType.getValidatorPath());
        return validator.validate(documentNumber);
    }

}