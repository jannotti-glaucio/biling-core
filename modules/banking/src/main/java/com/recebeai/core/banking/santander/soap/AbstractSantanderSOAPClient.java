package tech.jannotti.billing.core.banking.santander.soap;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.santander.soap.parser.XMLRequestParser;
import tech.jannotti.billing.core.banking.santander.soap.parser.XMLResponseParser;
import tech.jannotti.billing.core.connector.soap.AbstractSOAPClient;

public abstract class AbstractSantanderSOAPClient extends AbstractSOAPClient {

    @Autowired
    protected XMLRequestParser xmlRequestParser;

    @Autowired
    protected XMLResponseParser xmlResponseParser;

}
