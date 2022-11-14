package tech.jannotti.billing.core.connector.parser;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import org.springframework.xml.transform.StringSource;

import tech.jannotti.billing.core.connector.exception.ConnectorException;

public abstract class AbstractXMLParser {

    protected JAXBContext context;

    protected String enconding;

    public AbstractXMLParser(String enconding, String... contextPackages) {
        this.enconding = enconding;
        initContext(contextPackages);
    }

    private void initContext(String... contextPackages) {

        String contextPaths = null;
        for (String contextPackage : contextPackages) {
            if (contextPaths == null)
                contextPaths = contextPackage;
            else
                contextPaths += ":" + contextPackage;
        }

        try {
            this.context = JAXBContext.newInstance(contextPaths);
        } catch (JAXBException e) {
            throw new ConnectorParserException("Erro inicilizando contexto JAXB", e);
        }
    }

    private Marshaller initMarshaller(JAXBContext context, boolean formatted, boolean showHeader) {
        Marshaller marshaller = null;
        try {
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, enconding);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !showHeader);

        } catch (JAXBException e) {
            throw new ConnectorParserException("Erro configurando parser JAXB", e);
        }
        return marshaller;
    }

    public <T> String format(T request, boolean formatted, boolean showHeader) {
        return doFormat(request, formatted, showHeader);
    }

    public <T> String formatWihthouRoot(T request, Class<T> clazz, boolean formatted, boolean showHeader) {

        QName qName = new QName(request.getClass().getSimpleName());
        JAXBElement<T> root = new JAXBElement<>(qName, clazz, request);

        return doFormat(root, formatted, showHeader);
    }

    private <T> String doFormat(T request, boolean formatted, boolean showHeader) {

        Marshaller marshaller = initMarshaller(context, formatted, showHeader);

        StringWriter writer = new StringWriter();
        try {
            marshaller.marshal(request, writer);
        } catch (JAXBException e) {
            throw new ConnectorException("Erro efetuando parser de objeto para XML", e);
        }
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T parse(Source source) {

        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new ConnectorParserException("Erro configurando parser JAXB", e);
        }

        Object result = null;
        try {
            result = unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
            throw new ConnectorParserException("Erro efetuando parser de XML para objeto", e);
        }

        if (result instanceof JAXBElement) {
            JAXBElement<T> element = (JAXBElement<T>) result;
            return element.getValue();

        } else
            return (T) result;
    }

    public <T> T parse(String xml) {

        StringSource source = new StringSource(xml);
        return parse(source);
    }

}
