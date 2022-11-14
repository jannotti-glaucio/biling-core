package tech.jannotti.billing.core.connector.soap;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.springframework.ws.client.core.SourceExtractor;

import tech.jannotti.billing.core.connector.parser.AbstractXMLParser;

public abstract class AbstractXMLResponseExtractor<T> implements SourceExtractor<T> {

    private AbstractXMLParser xmlParser;

    public AbstractXMLResponseExtractor(AbstractXMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    @Override
    public T extractData(Source source) throws IOException, TransformerException {
        return xmlParser.parse(source);
    }

}
