package tech.jannotti.billing.core.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class NumberHelper {

    private static DecimalFormat buildDecimalFormat(String format, Character decimalSeparator) {
        DecimalFormat decimalFormat = new DecimalFormat(format);

        if (decimalSeparator != null) {
            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
            formatSymbols.setDecimalSeparator(decimalSeparator);
            decimalFormat.setDecimalFormatSymbols(formatSymbols);
        }
        return decimalFormat;
    }

    public static String format(Number number, String format, Character decimalSeparator) {

        if (number == null)
            return null;

        DecimalFormat decimalFormat = buildDecimalFormat(format, decimalSeparator);
        return decimalFormat.format(number);
    }

    public static String format(Number number, String format) {
        return format(number, format, null);
    }

    public static String formatAsDecimal(int number, String format, Character decimalSeparator) {

        BigDecimal decimal = new BigDecimal(number).setScale(2, RoundingMode.HALF_DOWN);
        decimal = decimal.divide(new BigDecimal(100));
        return format(decimal, format, decimalSeparator);
    }

    public static Number parse(String number, String format, Character decimalSeparator) throws ParseException {

        if (StringUtils.isBlank(number))
            return null;

        DecimalFormat decimalFormat = buildDecimalFormat(format, decimalSeparator);
        return decimalFormat.parse(StringUtils.trimToEmpty(number));
    }

    public static Integer parseAsInteger(String number, String format, Character decimalSeparator)
        throws ParseException {

        if (StringUtils.isBlank(number))
            return null;

        Number parsedNumber = parse(number, format, decimalSeparator);
        return fromDoubleToInteger(parsedNumber.doubleValue());
    }

    public static Integer parseAsInteger(String number) {
        if (StringUtils.isBlank(number))
            return null;

        return Integer.valueOf(StringUtils.trimToEmpty(number));
    }

    public static Long parseAsLong(String number) {
        if (StringUtils.isBlank(number))
            return null;

        return Long.valueOf(StringUtils.trimToEmpty(number));
    }

    public static int fromDoubleToInteger(double number) {
        BigDecimal decimal = new BigDecimal(number).setScale(2, RoundingMode.HALF_DOWN);
        decimal = decimal.multiply(new BigDecimal(100));
        return decimal.intValue();
    }

    public static int fromLongToInteger(Long number) {
        if (number == null)
            return 0;
        else
            return number.intValue();
    }

    public static Double fromIntegerToDouble(Integer number) {
        if (number == null)
            return null;

        BigDecimal decimal = new BigDecimal(number).setScale(2, RoundingMode.HALF_DOWN);
        decimal = decimal.divide(new BigDecimal(100));
        return decimal.doubleValue();
    }

    public static BigDecimal fromIntegerToBigDecimal(Integer number) {
        if (number == null)
            return null;

        BigDecimal decimal = new BigDecimal(number).setScale(2, RoundingMode.HALF_DOWN);
        decimal = decimal.divide(new BigDecimal(100));
        return decimal;
    }

    public static boolean contains(int number, int... values) {
        return ArrayUtils.contains(values, number);
    }

}
