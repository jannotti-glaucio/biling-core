package tech.jannotti.billing.core.commons.bean.parser;

import java.io.InputStream;
import java.io.StringReader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BeanJSONParser {

    private ObjectMapper objectMapper;

    public BeanJSONParser(Class<?>... subTypes) {
        objectMapper = new ObjectMapper();
        configureMapperDefaultProperties(objectMapper);
    }

    private void configureMapperDefaultProperties(ObjectMapper mapper, Class<?>... subTypes) {

        mapper.setSerializationInclusion(Include.NON_NULL);

        mapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY);

        if (subTypes.length > 0)
            mapper.registerSubtypes(subTypes);
    }

    public <T> String parseToJSON(T bean, boolean formatted) {

        ObjectWriter writer = null;
        if (!formatted)
            writer = objectMapper.writer();
        else
            writer = objectMapper.writer().withDefaultPrettyPrinter();

        try {
            return writer.writeValueAsString(bean);
        } catch (Exception e) {
            throw new BeanParserException("Erro efetuando parser de bean para JSON", e);
        }
    }

    public <T> String parseToJSON(T bean) {
        return parseToJSON(bean, false);
    }

    public <T> T parseFromJSON(Class<T> type, String json) {
        StringReader reader = new StringReader(json);

        T result;
        try {
            result = objectMapper.readValue(reader, type);
        } catch (Exception e) {
            throw new BeanParserException("Erro efetuando parser de bean para objeto", e);
        }
        return result;
    }

    public <T> T parseFromJSON(Class<T> type, InputStream stream) {

        T result;
        try {
            result = objectMapper.readValue(stream, type);
        } catch (Exception e) {
            throw new BeanParserException("Erro efetuando parser de bean para objeto", e);
        }
        return result;
    }

}