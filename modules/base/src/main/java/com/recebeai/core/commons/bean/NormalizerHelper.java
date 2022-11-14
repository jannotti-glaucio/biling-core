package tech.jannotti.billing.core.commons.bean;

import java.text.Normalizer;

import org.apache.commons.lang.StringUtils;

public class NormalizerHelper {

    public static String normalize(String value) {

        String normalizedValue = Normalizer.normalize(value, Normalizer.Form.NFD);

        normalizedValue = normalizedValue.replaceAll("[^a-zA-Z0-9 ,;]", "");

        return normalizedValue;
    }

    public static String normalize(String value, int size) {
        String normalizedValue = normalize(value);
        return StringUtils.substring(normalizedValue, 0, size);
    }

}
