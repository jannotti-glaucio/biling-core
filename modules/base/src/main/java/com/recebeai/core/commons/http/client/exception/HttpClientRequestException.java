package tech.jannotti.billing.core.commons.http.client.exception;

@SuppressWarnings("serial")
public class HttpClientRequestException extends Exception {

    private Integer httpStatus;

    public HttpClientRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientRequestException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

}
