package tech.jannotti.billing.core.banking.santander.soap.parser;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;

@Component("banking.santander.xmlRequestParser")
public class XMLRequestParser extends AbstractXMLParser {

    public XMLRequestParser() {
        super(SantanderConstants.SOAP_CHARSET,
            "tech.jannotti.billing.core.banking.santander.soap.stub.ticket",
            "tech.jannotti.billing.core.banking.santander.soap.stub.cobranca");
    }

}
