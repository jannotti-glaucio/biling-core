package tech.jannotti.billing.core.connector.rest;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import tech.jannotti.billing.core.commons.http.client.HttpClientFactory;
import tech.jannotti.billing.core.commons.security.SecurityDataHelper;
import tech.jannotti.billing.core.connector.rest.interceptor.BearerAuthorizationInterceptor;

public abstract class AbstractRestClient {

    @Autowired
    private HttpClientFactory httpClientFactory;

    private HttpComponentsClientHttpRequestFactory secureRequestFactory;

    private HttpComponentsClientHttpRequestFactory insecureRequestFactory;

    @PostConstruct
    public void init() {
        HttpClient secureHttpClient = httpClientFactory.createClient();
        secureRequestFactory = new HttpComponentsClientHttpRequestFactory(secureHttpClient);

        HttpClient insecureHttpClient = httpClientFactory.createClient(true);
        insecureRequestFactory = new HttpComponentsClientHttpRequestFactory(insecureHttpClient);
    }

    private RestTemplate getRestTemplate(boolean validateSSL) {

        HttpComponentsClientHttpRequestFactory requestFactory = null;
        if (validateSSL)
            requestFactory = secureRequestFactory;
        else
            requestFactory = insecureRequestFactory;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    protected RestTemplate getRestTemplate(String username, String password, boolean validateSSL) {

        RestTemplate restTemplate = getRestTemplate(validateSSL);

        BasicAuthorizationInterceptor authorizationInterceptor = new BasicAuthorizationInterceptor(username, password);
        restTemplate.getInterceptors().add(authorizationInterceptor);

        return restTemplate;
    }

    protected RestTemplate getRestTemplate(String token, boolean validateSSL) {

        RestTemplate restTemplate = getRestTemplate(validateSSL);

        BearerAuthorizationInterceptor authorizationInterceptor = new BearerAuthorizationInterceptor(token);
        restTemplate.getInterceptors().add(authorizationInterceptor);

        return restTemplate;
    }

    protected RestTemplate getRestTemplate(String token) {
        return getRestTemplate(token, false);
    }

    protected String maskJSONField(String requestJSON, String fieldName, String fieldValue) {
        return SecurityDataHelper.maskFieldInJSON(requestJSON, fieldName, fieldValue);
    }

}
