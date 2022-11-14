package tech.jannotti.billing.core.commons.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class SecurityDataHelper {

    public static String maskField(String field) {

        if (field != null) {
            String maskedField = StringUtils.leftPad("", field.length(), "X");
            return maskedField;
        } else {
            return null;
        }
    }

    public static String maskAndReplaceField(String content, String regex, String fieldValue) {

        String maskedField = maskField(fieldValue);

        String maskedContent = content.replaceAll(regex, maskedField);
        return maskedContent;
    }

    public static String maskFieldInJSON(String content, String fieldName, String fieldValue) {
        String regex = "(?<=\"(?i)" + fieldName + "(\":\"|\" : \"|\": \"|\" :\"))[^\"]++";

        return maskAndReplaceField(content, regex, fieldValue);
    }

    public static String maskFieldInJSON(String content, String fieldName) {

        String fieldValue = getJSONFieldValue(content, fieldName);
        return maskFieldInJSON(content, fieldName, fieldValue);
    }

    private static String getJSONFieldValue(String content, String fieldName) {

        String regex = "\"" + fieldName + "(\":\"|\" :\"|\" :\"|\" : \")(.+?)\"";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(2);
        else
            return "XXX";
    }

}