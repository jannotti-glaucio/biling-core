package tech.jannotti.billing.core.rest.security;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.bean.parser.BeanJSONParser;

@Component("security.responseParser")
public class ResponseParser {

    private BeanJSONParser beanJSONParser = new BeanJSONParser();

    public <T> T parseResponse(Class<T> type, InputStream stream) {
        return beanJSONParser.parseFromJSON(type, stream);
    }

}
