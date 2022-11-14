package tech.jannotti.billing.core.commons.beanio.handlers;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.beanio.types.DateTypeHandler;
import org.beanio.types.TypeConversionException;

public class CustomDateTypeHandler extends DateTypeHandler {

    private String nullReturnValue = null;

    @Override
    public Date parse(String text) throws TypeConversionException {

        if (StringUtils.isBlank(text))
            return super.parse("");

        if (constainsOnlyZeros(text)) {
            nullReturnValue = text;
            return super.parse("");
        }

        return super.parse(text);
    }

    @Override
    public String format(Object value) {
        if (value == null)
            return nullReturnValue;
        else
            return super.format(value);
    }

    private boolean constainsOnlyZeros(String text) {
        int zeros = StringUtils.countMatches(text, "0");
        if (zeros == text.length())
            return true;
        else
            return false;
    }

}
