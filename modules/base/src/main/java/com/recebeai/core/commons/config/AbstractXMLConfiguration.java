package tech.jannotti.billing.core.commons.config;

import java.net.URL;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

public abstract class AbstractXMLConfiguration {

    protected XMLConfiguration config;

    protected AbstractXMLConfiguration() {
        String filePath = getFilePath();
        URL url = this.getClass().getResource(filePath);

        XMLConfiguration.setDefaultListDelimiter('|');

        try {
            config = new XMLConfiguration(url);
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            throw new ConfigurationException("Erro lendo arquivo [" + url + "']", e);
        }

        config.setExpressionEngine(new XPathExpressionEngine());
    }

    protected abstract String getFilePath();

    protected String getStringProperty(String xpath) {
        String property = config.getString(xpath);
        return property;
    }

}
