package tech.jannotti.billing.core.connector.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.http.client.HttpClientHelper;
import tech.jannotti.billing.core.commons.http.client.HttpClientHelper.HttpResponse;
import tech.jannotti.billing.core.commons.http.client.exception.HttpClientRequestException;
import tech.jannotti.billing.core.connector.exception.ConnectorException;

@Component
public class ConnectorHttpClientHelper {

    @Autowired
    private HttpClientHelper httpClientHelper;

    public String sendPostHttpRawRequest(String url, String requestBody, ContentType contentType, String charset,
        String headerName, String headerValue, int timeout, boolean validateSSL) {

        Map<String, String> headers = null;
        if (headerName != null) {
            headers = new HashMap<String, String>();
            headers.put(headerName, headerValue);
        }

        String responseXml = null;
        try {
            HttpResponse httpResponse = httpClientHelper.sendPostHttpRequest(url, requestBody, contentType, charset, headers,
                timeout, validateSSL);
            responseXml = httpResponse.getResponseBody();

        } catch (HttpClientRequestException e) {
            throw new ConnectorException("Erro enviando requisicao HTTP", e);
        }
        return responseXml;
    }

    public String sendPostHttpRawRequest(String url, String requestBody, ContentType contentType, String charset, int timeout,
        boolean validateSSL) {
        return sendPostHttpRawRequest(url, requestBody, contentType, charset, null, null, timeout, validateSSL);
    }

    public String sendPostHttpFormRequest(String url, Map<String, String> parameters, ContentType contentType, String charset,
        int timeout, boolean validateSSL) {

        String responseXml = null;
        try {
            HttpResponse httpResponse = httpClientHelper.sendPostHttpRequest(url, parameters, contentType, charset, null, timeout,
                validateSSL);
            responseXml = httpResponse.getResponseBody();

        } catch (HttpClientRequestException e) {
            throw new ConnectorException("Erro enviando requisicao HTTP", e);
        }
        return responseXml;
    }

    public String sendPostHttpFormRequest(String url, String parameterName, String parameterValue, ContentType contentType,
        String charset, int timeout, boolean validateSSL) {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(parameterName, parameterValue);

        return sendPostHttpFormRequest(url, parameters, contentType, charset, timeout, validateSSL);
    }

}
