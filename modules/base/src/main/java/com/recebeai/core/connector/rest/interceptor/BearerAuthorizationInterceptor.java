package tech.jannotti.billing.core.connector.rest.interceptor;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import tech.jannotti.billing.core.commons.http.HttpConstants;

public class BearerAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private final String token;

    public BearerAuthorizationInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add(HttpHeaders.AUTHORIZATION, HttpConstants.BEARER_AUHTORIZATION_PREFIX + token);
        return execution.execute(request, body);
    }

}