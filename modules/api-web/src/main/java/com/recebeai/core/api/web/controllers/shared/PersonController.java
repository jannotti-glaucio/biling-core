package tech.jannotti.billing.core.api.web.controllers.shared;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.person.GetDocumentTypeRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.person.ListDocumentTypesRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.AddressService;
import tech.jannotti.billing.core.services.DocumentTypeService;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPersonType;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCountry;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "person")
public class PersonController extends AbstractWebController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping(value = "/documentType/{country}")
    @InfoLogging
    public ListDocumentTypesRestResponse listDocumentTypes(
        @PathVariable("country") @ValidCountry String countryCode) {

        BaseCountry country = addressService.getCountry(countryCode);
        List<BaseDocumentType> documentTypes = documentTypeService.list(country);

        BaseResultCode resultCode = getQueryResultCode(documentTypes);
        return new ListDocumentTypesRestResponse(resultCode, dtoMapper, documentTypes);
    }

    @GetMapping(value = "/documentType/{country}/{personType}")
    @InfoLogging
    @ValidateParameters
    public GetDocumentTypeRestResponse getDocumentType(
        @PathVariable("country") @ValidCountry String country,
        @PathVariable("personType") @ValidPersonType String personType) {

        // Criar uma anotacao pra validar country
        BaseCountry countryEntity = addressService.getCountry(country);
        PersonTypeEnum personTypeEnum = PersonTypeEnum.valueOf(personType);

        BaseDocumentType documentType = documentTypeService.get(countryEntity, personTypeEnum);

        BaseResultCode resultCode = getQueryResultCode(documentType);
        return new GetDocumentTypeRestResponse(resultCode, dtoMapper, documentType);
    }

    @GetMapping(value = "/documentType/{code}/validate/{number}")
    @InfoLogging
    public RestResponseDTO validateDocumentNumber(@PathVariable String code, @PathVariable String number) {

        BaseDocumentType documentType = documentTypeService.get(code);

        if (documentType == null)
            throw new ResultCodeControllerException(CODE_INVALID_DOCUMENT_TYPE_VALUE);

        if (documentTypeService.validate(documentType, number))
            return createSuccessResponse();
        else
            return createResponse(CODE_INVALID_DOCUMENT_NUMBER, number);
    }

}
