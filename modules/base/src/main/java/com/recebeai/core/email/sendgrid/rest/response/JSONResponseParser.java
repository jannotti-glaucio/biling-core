package tech.jannotti.billing.core.email.sendgrid.rest.response;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.bean.parser.BeanJSONParser;

@Component("sendGrid.jsonResponseParser")
public class JSONResponseParser {

    private BeanJSONParser beanJSONParser = new BeanJSONParser();

    public <T> T parseResponse(Class<T> type, String json) {
        return beanJSONParser.parseFromJSON(type, json);
    }

    public <T> String formatResponse(T response) {
        return beanJSONParser.parseToJSON(response);
    }

}
