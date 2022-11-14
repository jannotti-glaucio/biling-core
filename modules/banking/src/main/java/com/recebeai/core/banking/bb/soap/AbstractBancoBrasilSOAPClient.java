package tech.jannotti.billing.core.banking.bb.soap;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.bb.soap.parser.XMLRequestParser;
import tech.jannotti.billing.core.banking.bb.soap.parser.XMLResponseParser;
import tech.jannotti.billing.core.connector.soap.AbstractSOAPClient;

public abstract class AbstractBancoBrasilSOAPClient extends AbstractSOAPClient {

    @Autowired
    protected XMLRequestParser xmlRequestParser;

    @Autowired
    protected XMLResponseParser xmlResponseParser;

}
