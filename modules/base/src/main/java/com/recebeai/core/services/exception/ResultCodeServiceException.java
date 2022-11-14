package tech.jannotti.billing.core.services.exception;

import tech.jannotti.billing.core.commons.exception.ExceptionHelper;
import tech.jannotti.billing.core.commons.exception.ResultCodeException;

import lombok.Getter;

public class ResultCodeServiceException extends ServiceException implements ResultCodeException {
    private static final long serialVersionUID = 1L;

    private @Getter String resultCodeKey;
    private @Getter String[] parameters;

    public ResultCodeServiceException(String resultCodeKey, String... parameters) {
        this.resultCodeKey = resultCodeKey;
        this.parameters = parameters;
    }

    @Override
    public String getMessage() {
        return ExceptionHelper.resultCodeExceptionToMessage(this, resultCodeKey);
    }

    @Override
    public String toString() {
        return ExceptionHelper.resultCodeExceptionToString(this, resultCodeKey);
    }

}
