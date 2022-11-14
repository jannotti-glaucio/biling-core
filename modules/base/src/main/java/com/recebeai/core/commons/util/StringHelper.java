package tech.jannotti.billing.core.commons.util;

import java.text.Normalizer;

import org.apache.commons.lang.StringUtils;

public class StringHelper {

    public static boolean isBlanks(String... strings) {

        for (String string : strings) {
            if (StringUtils.isNotBlank(string))
                return false;
        }
        return true;
    }

    public static String replaceLineSeparators(String symbol, String string) {
        String lineSeparator = System.getProperty("line.separator");

        String result = StringUtils.replace(string, symbol, lineSeparator);
        return result;
    }

    public static String removesAccents(String value) {
        value = Normalizer.normalize(value, Normalizer.Form.NFD);
        return value.replaceAll("[^\\p{ASCII}]", "");
    }

}