package tech.jannotti.billing.core.email.sendgrid;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.AbstractConnector;
import tech.jannotti.billing.core.connector.email.EMailConnector;
import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.email.sendgrid.rest.SendGridRestClient;
import tech.jannotti.billing.core.email.sendgrid.rest.request.dto.SendEmailRequest;
import tech.jannotti.billing.core.email.sendgrid.rest.request.dto.SendEmailRequest.Attachment;
import tech.jannotti.billing.core.email.sendgrid.rest.request.dto.SendEmailRequest.Personalization;
import tech.jannotti.billing.core.email.sendgrid.rest.response.dto.SendEmailResponse;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;

@Component("emailConnectors.sendGrid")
public class SendGridEMailConnector extends AbstractConnector implements EMailConnector {

    private static final LogManager logManager = LogFactory.getManager(SendGridEMailConnector.class);

    @Value("${core.email.sendGrid.url}")
    private String url;

    @Value("${core.email.sendGrid.apiKey}")
    private String apiKey;

    @Autowired
    private TemplatesConfig templatesConfig;

    @Autowired
    private SendGridRestClient sendGridRestClient;

    @Override
    public ConnectorResponseDTO sendEmail(String from, String to, BaseLanguage language, String template,
        Map<String, String> templateProperties, byte[] attachment) {

        String templateId = templatesConfig.getTemplateId(language.getCode(), template);
        String attachmentName = templatesConfig.getTemplateAttachmentName(language.getCode(), template);

        SendEmailRequest request = new SendEmailRequest();
        request.getFrom().setEmail(from);
        request.setTemplateId(templateId);

        // Customizacoes
        Personalization personalization = new Personalization();
        // Destinatarios
        personalization.addTO(to);
        // Variaveis do template
        personalization.setDynamicTemplateData(templateProperties);
        request.getPersonalizations().add(personalization);

        // Anexo
        if (attachment != null) {
            List<Attachment> attachmentRequests = new ArrayList<Attachment>();

            Attachment attachmentRequest = new Attachment();
            String encodedAttachment = Base64.getEncoder().encodeToString(attachment);
            attachmentRequest.setContent(encodedAttachment);
            attachmentRequest.setFilename(attachmentName);
            attachmentRequests.add(attachmentRequest);

            request.setAttachments(attachmentRequests);
        }

        SendEmailResponse response = sendGridRestClient.sendEmail(url, apiKey, request);

        if ((response != null) && (response.getError() != null)) {
            logManager.logERROR("Erro enviando email: %s", response.getError());
            return new ConnectorResponseDTO(getGenericErrorResultCode());

        } else
            return new ConnectorResponseDTO(getSuccessResultCode());
    }

}
