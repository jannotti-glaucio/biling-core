package tech.jannotti.billing.core.commons.exception;

import org.apache.commons.lang.StringUtils;

public class ExceptionHelper {

    public static Exception toException(ResultCodeException e) {
        Exception ex = (Exception) e;
        return ex;
    }

    public static String resultCodeExceptionToMessage(Exception e, String resultCodeKey) {

        if (StringUtils.isNotBlank(resultCodeKey))
            return "[resultCodeKey=" + resultCodeKey + "]";
        else
            return null;
    }

    public static String resultCodeExceptionToString(Exception e, String resultCodeKey) {

        String name = e.getClass().getName();
        String message = e.getMessage();
        return name + " " + message;
    }

}
