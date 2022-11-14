package tech.jannotti.billing.core.banking.bb.soap.parser;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;

@Component("banking.bb.xmlResponseParser")
public class XMLResponseParser extends AbstractXMLParser {

    public XMLResponseParser() {
        super(BancoBrasilConstants.SOAP_CHARSET,
            "tech.jannotti.billing.core.banking.bb.soap.stub.registro");
    }

}
