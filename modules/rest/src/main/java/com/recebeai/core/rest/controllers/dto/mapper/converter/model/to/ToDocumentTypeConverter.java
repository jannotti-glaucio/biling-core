package tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.AbstractToModelConverter;
import tech.jannotti.billing.core.services.DocumentTypeService;

@Component
public class ToDocumentTypeConverter extends AbstractToModelConverter<BaseDocumentType> {

    @Autowired
    private DocumentTypeService documentTypeService;

    @Override
    protected BaseDocumentType convert(String source) {
        if (source == null)
            return null;

        return documentTypeService.get(source);
    }

}
