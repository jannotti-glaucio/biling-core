package tech.jannotti.billing.core.connector.soap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor;
import org.springframework.xml.transform.StringSource;

import tech.jannotti.billing.core.commons.http.client.HttpClientFactory;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;
import tech.jannotti.billing.core.connector.soap.callback.SOAPRequestCallback;

public abstract class AbstractSOAPClient {

    private static final LogManager logManager = LogFactory.getManager(AbstractSOAPClient.class);

    @Autowired
    private HttpClientFactory httpClientFactory;

    private SOAPGatewaySupport secureSOAPGatewaySupport;

    private SOAPGatewaySupport insecureSOAPGatewaySupport;

    @PostConstruct
    public void init() {

        HttpClient secureHttpClient = httpClientFactory.createClient(new RemoveSoapHeadersInterceptor());
        HttpComponentsMessageSender secureMessageSender = new HttpComponentsMessageSender(secureHttpClient);
        secureSOAPGatewaySupport = new SOAPGatewaySupport(secureMessageSender, null);

        HttpClient insecureHttpClient = httpClientFactory.createClient(false, new RemoveSoapHeadersInterceptor());
        HttpComponentsMessageSender insecureMessageSender = new HttpComponentsMessageSender(insecureHttpClient);
        insecureSOAPGatewaySupport = new SOAPGatewaySupport(insecureMessageSender, null);
    }

    @SuppressWarnings("unchecked")
    private <T, I> T sendAndReceive(String url, String accessToken, I request, Class<I> requestType, String soapAction,
        int requestTimeout, boolean validateSSL, Class<T> responseType) {

        WebServiceTemplate webServiceTemplate = getWebServiceTemplate(validateSSL);
        SOAPRequestCallback requestCallback = new SOAPRequestCallback(soapAction, requestTimeout);

        if (StringUtils.isNotBlank(accessToken))
            requestCallback.setToken(accessToken);

        String plainRequest = null;
        if (requestType == null)
            plainRequest = getXmlRequestParser().format(request, false, false);
        else
            plainRequest = getXmlRequestParser().formatWihthouRoot(request, requestType, false, false);
        logManager.logDEBUG("Enviando requisicao SOAP para [%s]: %s", url, plainRequest);

        StringSource requestSource = new StringSource(plainRequest);
        AbstractXMLResponseExtractor<?> responseExtractor = buildXMLResponseExtractor();

        T response = null;
        try {
            response = (T) webServiceTemplate.sendSourceAndReceive(url, requestSource, requestCallback, responseExtractor);

        } catch (SoapFaultClientException e) {
            logManager.logERROR("Falha SOAP recebido na resposta [%s]", e.getFaultStringOrReason());
            throw new SOAPClientException("Falha SOAP enviando requisicao", e);

        } catch (Exception e) {
            throw new SOAPClientException("Erro enviando requisicao SOAP", e);
        }

        String plainResponse = null;
        if (responseType == null)
            plainResponse = getXmlResponseParser().format(response, false, false);
        else
            plainResponse = getXmlResponseParser().formatWihthouRoot(response, responseType, false, false);
        logManager.logDEBUG("Resposta SOAP recebida: %s", plainResponse);
        return response;
    }

    public <T, I> T sendAndReceive(String url, String accessToken, I request, String soapAction, int requestTimeout,
        boolean validateSSL) {
        return sendAndReceive(url, accessToken, request, null, soapAction, requestTimeout, validateSSL, null);
    }

    public <T, I> T sendAndReceive(String url, I request, Class<I> requestType, String soapAction, int requestTimeout,
        Class<T> responseType) {
        return sendAndReceive(url, null, request, requestType, soapAction, requestTimeout, true, responseType);
    }

    private final WebServiceTemplate getWebServiceTemplate(boolean validateSSL) {
        if (validateSSL)
            return secureSOAPGatewaySupport.getWebServiceTemplate();
        else
            return insecureSOAPGatewaySupport.getWebServiceTemplate();
    }

    protected abstract AbstractXMLParser getXmlRequestParser();

    protected abstract AbstractXMLParser getXmlResponseParser();

    protected abstract AbstractXMLResponseExtractor<?> buildXMLResponseExtractor();

}
