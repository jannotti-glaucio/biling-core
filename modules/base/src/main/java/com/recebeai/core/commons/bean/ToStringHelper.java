package tech.jannotti.billing.core.commons.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import tech.jannotti.billing.core.commons.security.SecurityDataHelper;

public class ToStringHelper {

    private ToStringBuilder toStringBuilder;

    public static ToStringHelper build(Object object) {
        return new ToStringHelper(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private ToStringHelper(Object object, ToStringStyle toStringStyle) {
        toStringBuilder = new ToStringBuilder(object, toStringStyle);
    }

    public ToStringHelper add(String name, Object value) {
        if (value != null)
            toStringBuilder.append(name, value);
        return this;
    }

    public ToStringHelper add(String name, long value) {
        toStringBuilder.append(name, value);
        return this;
    }

    public ToStringHelper add(String name, int value) {
        toStringBuilder.append(name, value);
        return this;
    }

    public ToStringHelper add(String name, boolean value) {
        toStringBuilder.append(name, value);
        return this;
    }

    public ToStringHelper mask(String name, String value) {
        if (value != null)
            toStringBuilder.append(name, SecurityDataHelper.maskField(value));
        return this;
    }

    public String toString() {
        return toStringBuilder.toString();
    }

    public static String toString(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}