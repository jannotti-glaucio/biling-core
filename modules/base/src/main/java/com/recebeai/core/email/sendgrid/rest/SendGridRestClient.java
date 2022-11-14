package tech.jannotti.billing.core.email.sendgrid.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.rest.AbstractRestClient;
import tech.jannotti.billing.core.connector.rest.RestClientException;
import tech.jannotti.billing.core.email.sendgrid.SendGridEMailConnector;
import tech.jannotti.billing.core.email.sendgrid.rest.request.JSONRequestParser;
import tech.jannotti.billing.core.email.sendgrid.rest.request.dto.SendEmailRequest;
import tech.jannotti.billing.core.email.sendgrid.rest.response.JSONResponseParser;
import tech.jannotti.billing.core.email.sendgrid.rest.response.dto.SendEmailResponse;

@Component
public class SendGridRestClient extends AbstractRestClient {

    @Autowired
    private JSONResponseParser jsonResponseParser;

    @Autowired
    private JSONRequestParser jsonRequestParser;

    private static final LogManager logManager = LogFactory.getManager(SendGridEMailConnector.class);

    public SendEmailResponse sendEmail(String url, String apiKey, SendEmailRequest request) {

        String plainRequest = jsonRequestParser.parseRequest(request);
        // TODO Gravar todo o JSON de request no banco

        String maskedPlainRequest = maskJSONField(plainRequest, "content", "XXX");
        logManager.logDEBUG("Enviando requisicao para [%s]: %s", url, maskedPlainRequest);

        RestTemplate restTemplate = getRestTemplate(apiKey);

        ResponseEntity<SendEmailResponse> response = null;
        try {
            response = restTemplate.postForEntity(url, request, SendEmailResponse.class);
        } catch (RestClientResponseException e) {
            throw new RestClientException("Erro enviando requisicao", e);
        }

        if (response.getBody() == null) {
            logManager.logDEBUG("Resposta recebida [httpStatus=%s]", response.getStatusCodeValue());
            // TODO Gravar o header message-id no banco
            return null;

        } else {
            String plainResponse = jsonResponseParser.formatResponse(response.getBody());
            logManager.logDEBUG("Resposta recebida [%s]", plainResponse);
            // TODO Gravar todo o JSON de response no banco

            return response.getBody();
        }
    }

}
