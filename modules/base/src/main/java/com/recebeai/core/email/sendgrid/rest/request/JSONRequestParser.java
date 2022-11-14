package tech.jannotti.billing.core.email.sendgrid.rest.request;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.bean.parser.BeanJSONParser;

@Component("sendGrid.jsonRequestParser")
public class JSONRequestParser {

    private BeanJSONParser beanJSONParser = new BeanJSONParser();

    public <T> String parseRequest(T bean) {
        return beanJSONParser.parseToJSON(bean, false);
    }

}
