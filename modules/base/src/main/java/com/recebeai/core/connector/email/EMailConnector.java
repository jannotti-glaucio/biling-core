package tech.jannotti.billing.core.connector.email;

import java.util.Map;

import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;

public interface EMailConnector {

    public ConnectorResponseDTO sendEmail(String from, String to, BaseLanguage language, String template,
        Map<String, String> templateProperties, byte[] attachment);

}
