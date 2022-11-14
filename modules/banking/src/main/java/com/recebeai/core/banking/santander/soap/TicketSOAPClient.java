package tech.jannotti.billing.core.banking.santander.soap;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.Create;
import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.CreateResponse;
import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.TicketRequest;
import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.TicketResponse;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;
import tech.jannotti.billing.core.connector.soap.AbstractXMLResponseExtractor;

@Component
public class TicketSOAPClient extends AbstractSantanderSOAPClient {

    private static final String SOAP_ACTION = "create";

    public TicketResponse create(String url, TicketRequest request, int requestTimeout) {

        Create createRequest = new Create();
        createRequest.setTicketRequest(request);

        CreateResponse response = sendAndReceive(url, createRequest, Create.class, SOAP_ACTION, requestTimeout,
            CreateResponse.class);
        return response.getTicketResponse();
    }

    @Override
    protected AbstractXMLParser getXmlRequestParser() {
        return xmlRequestParser;
    }

    @Override
    protected AbstractXMLParser getXmlResponseParser() {
        return xmlResponseParser;
    }

    public static class CreateResponseExtractor extends AbstractXMLResponseExtractor<CreateResponse> {
        public CreateResponseExtractor(AbstractXMLParser xmlParser) {
            super(xmlParser);
        }
    }

    @Override
    protected AbstractXMLResponseExtractor<?> buildXMLResponseExtractor() {
        return new CreateResponseExtractor(xmlResponseParser);
    }

}
