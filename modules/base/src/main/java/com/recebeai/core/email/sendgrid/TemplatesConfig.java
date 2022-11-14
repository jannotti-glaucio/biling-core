package tech.jannotti.billing.core.email.sendgrid;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.config.AbstractXMLConfiguration;

@Component("sendGrid.templatesConfig")
public class TemplatesConfig extends AbstractXMLConfiguration {

    @Override
    protected String getFilePath() {
        return "/email/template/sendgrid-templates.xml";
    }

    private String buildXPath(String language, String name) {
        return String.format("templates/template[@language='%s' and @name='%s']", language, name);
    }

    public String getTemplateId(String language, String name) {
        String xpath = buildXPath(language, name) + "/id";
        return this.getStringProperty(xpath);
    }

    public String getTemplateAttachmentName(String language, String name) {
        String xpath = buildXPath(language, name) + "/attachment.name";
        return this.getStringProperty(xpath);
    }

}
