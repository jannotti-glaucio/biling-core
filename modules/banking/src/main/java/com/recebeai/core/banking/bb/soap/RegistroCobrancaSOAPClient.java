package tech.jannotti.billing.core.banking.bb.soap;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.soap.stub.registro.Requisicao;
import tech.jannotti.billing.core.banking.bb.soap.stub.registro.Resposta;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;
import tech.jannotti.billing.core.connector.soap.AbstractXMLResponseExtractor;

@Component
public class RegistroCobrancaSOAPClient extends AbstractBancoBrasilSOAPClient {

    private static final String SOAP_ACTION = "registrarBoleto";

    public Resposta registrarBoleto(String url, String accessToken, Requisicao requisicao, int requestTimeout,
        boolean validateSSL) {

        Resposta resposta = sendAndReceive(url, accessToken, requisicao, SOAP_ACTION, requestTimeout, validateSSL);
        return resposta;
    }

    @Override
    protected AbstractXMLParser getXmlRequestParser() {
        return xmlRequestParser;
    }

    @Override
    protected AbstractXMLParser getXmlResponseParser() {
        return xmlResponseParser;
    }

    public static class RespostaExtractor extends AbstractXMLResponseExtractor<Resposta> {
        public RespostaExtractor(AbstractXMLParser xmlParser) {
            super(xmlParser);
        }
    }

    @Override
    protected AbstractXMLResponseExtractor<?> buildXMLResponseExtractor() {
        return new RespostaExtractor(xmlResponseParser);
    }

}
