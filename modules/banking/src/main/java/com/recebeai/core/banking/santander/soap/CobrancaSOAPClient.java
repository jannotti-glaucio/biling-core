package tech.jannotti.billing.core.banking.santander.soap;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.RegistraTitulo;
import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.RegistraTituloResponse;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;
import tech.jannotti.billing.core.connector.soap.AbstractXMLResponseExtractor;

@Component
public class CobrancaSOAPClient extends AbstractSantanderSOAPClient {

    private static final String SOAP_ACTION = "registraTitulo";

    public RegistraTituloResponse registraTitulo(String url, RegistraTitulo request, int requestTimeout) {

        RegistraTituloResponse response = sendAndReceive(url, request, RegistraTitulo.class, SOAP_ACTION, requestTimeout,
            RegistraTituloResponse.class);
        return response;
    }

    @Override
    protected AbstractXMLParser getXmlRequestParser() {
        return xmlRequestParser;
    }

    @Override
    protected AbstractXMLParser getXmlResponseParser() {
        return xmlResponseParser;
    }

    public static class RegistraTituloResponseExtractor extends AbstractXMLResponseExtractor<RegistraTituloResponse> {
        public RegistraTituloResponseExtractor(AbstractXMLParser xmlParser) {
            super(xmlParser);
        }
    }

    @Override
    protected AbstractXMLResponseExtractor<?> buildXMLResponseExtractor() {
        return new RegistraTituloResponseExtractor(xmlResponseParser);
    }

}
