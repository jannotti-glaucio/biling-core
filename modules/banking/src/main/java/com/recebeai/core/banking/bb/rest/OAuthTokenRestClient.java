package tech.jannotti.billing.core.banking.bb.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import tech.jannotti.billing.core.banking.bb.rest.response.OAuthTokenResponse;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.rest.AbstractRestClient;

@Component
public class OAuthTokenRestClient extends AbstractRestClient {

    private static final LogManager logManager = LogFactory.getManager(OAuthTokenRestClient.class);

    public OAuthTokenResponse getToken(String url, String clientId, String secret, int requestTimeout, boolean validateSSL) {

        RestTemplate restTemplate = getRestTemplate(clientId, secret, validateSSL);
        HttpEntity<MultiValueMap<String, String>> request = buildAuthenticateRequest();

        logManager.logDEBUG("Enviando requisicao de Token [%s]", url);
        OAuthTokenResponse response = restTemplate.postForObject(url, request, OAuthTokenResponse.class);

        logManager.logDEBUG("Token recebido com sucesso");
        return response;
    }

    private HttpEntity<MultiValueMap<String, String>> buildAuthenticateRequest() {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("grant_type", "client_credentials");
        form.add("scope", "cobranca.registro-boletos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<MultiValueMap<String, String>>(form, headers);
    }

}
