package tech.jannotti.billing.core.commons.util;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class TextFormatterHelper {

    private static final LogManager logManager = LogFactory.getManager(TextFormatterHelper.class);

    public static String format(String mask, String value) {

        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setValueContainsLiteralCharacters(false);
            return formatter.valueToString(value);

        } catch (ParseException e) {
            logManager.logWARN("Erro formatando texto [mask=%s, value=%s]", mask, value);
            return value;
        }
    }

}
