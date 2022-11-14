package tech.jannotti.billing.core.connector;

import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.services.ResultCodeService;

public abstract class AbstractConnector {

    @Autowired
    protected ResultCodeService resultCodeService;

    protected BaseResultCode getSuccessResultCode() {
        return resultCodeService.getSuccessResultCode();
    }

    protected BaseResultCode getGenericErrorResultCode() {
        return resultCodeService.getGenericErrorResultCode();
    }

    protected BaseResultCode getResultCode(String resultCodeKey) {
        return resultCodeService.getByKey(resultCodeKey);
    }

    protected ConnectorResponseDTO createResponse(BaseResultCode resultCode, String... parameters) {
        ConnectorResponseDTO response = new ConnectorResponseDTO(resultCode, parameters);
        return response;
    }

    protected ConnectorResponseDTO createSuccessResponse() {
        BaseResultCode resultCode = resultCodeService.getSuccessResultCode();
        return createResponse(resultCode);
    }

    protected String processTemplate(String templateContent, Map<String, String> templateProperties) {
        StrSubstitutor substitutor = new StrSubstitutor(templateProperties);
        return substitutor.replace(templateContent);
    }

}
