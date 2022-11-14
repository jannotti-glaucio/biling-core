package tech.jannotti.billing.core.connector.soap.callback;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import tech.jannotti.billing.core.commons.http.HttpConstants;

import lombok.Setter;

public class SOAPRequestCallback implements WebServiceMessageCallback {

    private String soapAction;

    private int requestTimeout;

    @Setter
    private String token;

    public SOAPRequestCallback(String soapAction, int requestTimeout) {
        this.soapAction = soapAction;
        this.requestTimeout = requestTimeout;
    }

    public SOAPRequestCallback(int requestTimeout) {
        this("", requestTimeout);
    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

        TransportContext context = TransportContextHolder.getTransportContext();
        HttpComponentsConnection connection = (HttpComponentsConnection) context.getConnection();

        // Configura os timeouts
        RequestConfig postConfig = RequestConfig.custom()
            .setConnectTimeout(requestTimeout)
            .setSocketTimeout(requestTimeout)
            .build();
        connection.getHttpPost().setConfig(postConfig);

        // Configura o token de autenticacao
        if (StringUtils.isNotBlank(token))
            connection.getHttpPost().addHeader(HttpHeaders.AUTHORIZATION, HttpConstants.BEARER_AUHTORIZATION_PREFIX + token);

        // Seta o soapAction
        SoapMessage soapMessage = (SoapMessage) message;
        soapMessage.setSoapAction(soapAction);
    }

}
