package tech.jannotti.billing.core.commons.http.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.http.client.exception.HttpClientRequestException;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

@Component
public class HttpClientHelper {

    private static final LogManager logManager = LogFactory.getManager(HttpClientHelper.class);

    @Autowired
    private HttpClientFactory httpClientFactory;

    private CloseableHttpClient secureHttpClient;

    private CloseableHttpClient insecureHttpClient;

    public static class HttpResponse {

        private Integer httpStatus;
        private String responseBody;

        public HttpResponse(Integer httpStatus, String responseBody) {
            this.httpStatus = httpStatus;
            this.responseBody = responseBody;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public String getResponseBody() {
            return responseBody;
        }
    }

    @PostConstruct
    public void start() {
        secureHttpClient = httpClientFactory.createClient(true);
        insecureHttpClient = httpClientFactory.createClient();
    }

    @PreDestroy
    public void stop() {
        try {
            if (secureHttpClient != null)
                secureHttpClient.close();
        } catch (IOException e) {
            logManager.logERROR("Erro fechando conexao HTTP", e);
        }

        try {
            if (insecureHttpClient != null)
                insecureHttpClient.close();
        } catch (IOException e) {
            logManager.logERROR("Erro fechando conexao HTTP", e);
        }
    }

    private HttpResponse sendPostHttpRequest(String url, String requestBody, ContentType contentType, String charset,
        Map<String, String> headers, int requestTimeout, int[] acceptedHttpStatus, boolean validateSSL)
        throws HttpClientRequestException {

        HttpPost httpPost = createPost(url, requestTimeout);
        setRequestBody(httpPost, contentType, charset, requestBody);
        setHeaders(httpPost, headers);

        CloseableHttpClient httpClient = getHttpClient(validateSSL);

        HttpResponse response = sendRequest(httpClient, url, httpPost, acceptedHttpStatus);
        return response;
    }

    public HttpResponse sendPostHttpRequest(String url, String requestBody, ContentType contentType, String charset,
        Map<String, String> headers, int requestTimeout, boolean validateSSL) throws HttpClientRequestException {

        return sendPostHttpRequest(url, requestBody, contentType, charset, headers, requestTimeout,
            new int[] { HttpStatus.SC_OK }, validateSSL);
    }

    public HttpResponse sendPostHttpRequest(String url, String requestBody, ContentType contentType, String charset,
        Map<String, String> headers, int requestTimeout) throws HttpClientRequestException {

        return sendPostHttpRequest(url, requestBody, contentType, charset, headers, requestTimeout,
            new int[] { HttpStatus.SC_OK }, false);
    }

    public HttpResponse sendPostHttpRequest(String url, Map<String, String> parameters, ContentType contentType,
        String charset, Map<String, String> headers, int requestTimeout, boolean validateSSL)
        throws HttpClientRequestException {

        HttpPost httpPost = createPost(url, requestTimeout);
        createForm(httpPost, charset, parameters);
        setHeaders(httpPost, headers);

        CloseableHttpClient httpClient = getHttpClient(validateSSL);

        HttpResponse response = sendRequest(httpClient, url, httpPost, new int[] { HttpStatus.SC_OK });
        return response;
    }

    private HttpResponse sendGetHttpRequest(String url, Map<String, String> headers, int requestTimeout,
        int[] acceptedHttpStatus, boolean validateSSL) throws HttpClientRequestException {

        HttpGet httpGet = createGet(url, requestTimeout);
        setHeaders(httpGet, headers);

        CloseableHttpClient httpClient = getHttpClient(validateSSL);

        HttpResponse response = sendRequest(httpClient, url, httpGet, acceptedHttpStatus);
        return response;
    }

    public HttpResponse sendGetHttpRequest(String url, Map<String, String> headers, int requestTimeout)
        throws HttpClientRequestException {
        return sendGetHttpRequest(url, headers, requestTimeout, new int[] { HttpStatus.SC_OK }, false);
    }

    private CloseableHttpClient getHttpClient(boolean validateSSL) {
        if (validateSSL)
            return secureHttpClient;
        else
            return insecureHttpClient;
    }

    private HttpPost createPost(String url, int requestTimeout) {

        HttpPost httpPost = new HttpPost(url);

        RequestConfig postConfig = RequestConfig.custom().setConnectTimeout(requestTimeout)
            .setSocketTimeout(requestTimeout).build();
        httpPost.setConfig(postConfig);

        return httpPost;
    }

    private HttpGet createGet(String url, int requestTimeout) {

        HttpGet httpGet = new HttpGet(url);

        RequestConfig postConfig = RequestConfig.custom().setConnectTimeout(requestTimeout)
            .setSocketTimeout(requestTimeout).build();
        httpGet.setConfig(postConfig);

        return httpGet;
    }

    private void createForm(HttpPost httpPost, String charset, Map<String, String> parameters) {

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            formParams.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
        }

        UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(formParams, Charset.forName(charset));
        httpPost.setEntity(requestEntity);
    }

    private void setRequestBody(HttpPost httpPost, ContentType contentType, String charset, String requestBody) {

        StringEntity requestEntity = new StringEntity(requestBody, Charset.forName(charset));

        String entityContentType = ContentType.create(contentType.getMimeType(), charset).toString();
        requestEntity.setContentType(entityContentType);
        httpPost.setEntity(requestEntity);
    }

    private void setDefaultHeaders(HttpRequestBase httpRequest) {
        httpRequest.addHeader("Cache-Control", "no-cache");
    }

    private void setHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
        setDefaultHeaders(httpRequest);

        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {

                Header header = new BasicHeader(headerEntry.getKey(), headerEntry.getValue());
                httpRequest.addHeader(header);
            }
        }
    }

    private HttpResponse sendRequest(CloseableHttpClient httpClient, String url, HttpRequestBase httpRequest,
        int[] acceptedHttpStatus) throws HttpClientRequestException {

        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(acceptedHttpStatus);
        try {
            String responseBody = httpClient.execute(httpRequest, httpResponseHandler);
            int httpStatus = httpResponseHandler.getHttpStatus();

            HttpResponse httpResponse = new HttpResponse(httpStatus, responseBody);
            return httpResponse;

        } catch (ClientProtocolException cpe) {
            int httpStatus = httpResponseHandler.getHttpStatus();
            throw new HttpClientRequestException("Erro enviando requisicao HTTP", cpe, httpStatus);

        } catch (IOException e) {
            throw new HttpClientRequestException("Erro enviando requisicao HTTP", e);
        }
    }

}
