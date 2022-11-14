package tech.jannotti.billing.core.banking.bb.soap.parser;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;

@Component("banking.bb.xmlRequestParser")
public class XMLRequestParser extends AbstractXMLParser {

    public XMLRequestParser() {
        super(BancoBrasilConstants.SOAP_CHARSET,
            "tech.jannotti.billing.core.banking.bb.soap.stub.registro");
    }

}
