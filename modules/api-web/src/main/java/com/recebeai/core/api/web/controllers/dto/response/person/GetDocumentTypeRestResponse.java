package tech.jannotti.billing.core.api.web.controllers.dto.response.person;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.DocumentTypeDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class GetDocumentTypeRestResponse extends RestResponseDTO {

    private DocumentTypeDTO documentType;

    public GetDocumentTypeRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseDocumentType documentType) {
        super(resultCode);
        this.documentType = dtoMapper.map(documentType, DocumentTypeDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("documentType", documentType);
    }

}