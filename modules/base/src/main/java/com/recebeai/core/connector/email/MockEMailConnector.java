package tech.jannotti.billing.core.connector.email;

import java.util.Map;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.connector.AbstractConnector;
import tech.jannotti.billing.core.connector.email.response.SendEmailConnectorResponse;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;

@Component("mailConnectors.mock")
public class MockEMailConnector extends AbstractConnector implements EMailConnector {

    @Override
    public SendEmailConnectorResponse sendEmail(String from, String to, BaseLanguage language, String templateId,
        Map<String, String> templateProperties, byte[] attachment) {

        Long messageId = System.currentTimeMillis();
        return new SendEmailConnectorResponse(getSuccessResultCode(), messageId.toString());
    }

}
