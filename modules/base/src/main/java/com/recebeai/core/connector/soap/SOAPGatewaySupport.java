package tech.jannotti.billing.core.connector.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

public class SOAPGatewaySupport extends WebServiceGatewaySupport {

    public SOAPGatewaySupport(HttpComponentsMessageSender messageSender, Jaxb2Marshaller marshaller) {
        super();
        setMessageSender(messageSender);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

}
