package tech.jannotti.billing.core.banking.santander.soap.parser;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;

@Component("banking.santander.xmlResponseParser")
public class XMLResponseParser extends AbstractXMLParser {

    public XMLResponseParser() {
        super(SantanderConstants.SOAP_CHARSET,
            "tech.jannotti.billing.core.banking.santander.soap.stub.ticket",
            "tech.jannotti.billing.core.banking.santander.soap.stub.cobranca");
    }

}
