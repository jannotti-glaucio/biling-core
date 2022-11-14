package tech.jannotti.billing.core.api.web.controllers.dto.response.person;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.DocumentTypeDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class ListDocumentTypesRestResponse extends RestResponseDTO {

    private List<DocumentTypeDTO> documentTypes;

    public ListDocumentTypesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<BaseDocumentType> documentType) {
        super(resultCode);
        this.documentTypes = dtoMapper.mapList(documentType, DocumentTypeDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("documentTypes.size", CollectionUtils.size(documentTypes));
    }

}