package tech.jannotti.billing.core.rest.controllers.handlers;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import tech.jannotti.billing.core.commons.exception.ExceptionHelper;
import tech.jannotti.billing.core.commons.exception.ResultCodeException;
import tech.jannotti.billing.core.commons.http.HttpHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.services.ResultCodeService;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final LogManager logManager = LogFactory.getManager(ControllerExceptionHandler.class);

    @Autowired
    private ResultCodeService resultCodeService;

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<RestResponseDTO> handleException(Exception e) {
        return doHandleException(e);
    }

    @ExceptionHandler({ ResultCodeControllerException.class, ResultCodeServiceException.class })
    public ResponseEntity<RestResponseDTO> handleResultCodeException(ResultCodeException e) {
        return doHandleResultCodeException(e);
    }

    private ResponseEntity<RestResponseDTO> doHandleResultCodeException(ResultCodeException e) {

        String url = HttpHelper.getHttpRequest().getRequestURL().toString();
        String key = e.getResultCodeKey();
        String[] parameters = e.getParameters();

        BaseResultCode resultCode = null;
        if (StringUtils.isNotBlank(key)) {
            resultCode = resultCodeService.getByKey(key);
            logManager.logINFO("Finalizando execucao de requisicao de [%s] com retorno = [%s - %s]", url,
                resultCode.getKey(), resultCode.getMessage());

        } else {
            Exception ex = ExceptionHelper.toException(e);
            logManager.logERROR("Erro executando requisicao de [%s]", ex, url);
            resultCode = resultCodeService.getGenericErrorResultCode();
        }

        return buildResponseEntity(resultCode, parameters);
    }

    @ExceptionHandler({ MappingException.class })
    public ResponseEntity<RestResponseDTO> handleMappingException(MappingException e) {

        if (e.getCause() instanceof ResultCodeServiceException) {
            ResultCodeServiceException resultCodeException = (ResultCodeServiceException) e.getCause();
            return doHandleResultCodeException(resultCodeException);
        } else
            return doHandleException(e);
    }

    private ResponseEntity<RestResponseDTO> doHandleException(Exception e) {

        String url = HttpHelper.getHttpRequest().getRequestURL().toString();
        logManager.logERROR("Erro executando requisicao de [%s]", e, url);

        BaseResultCode resultCode = resultCodeService.getGenericErrorResultCode();
        return buildResponseEntity(resultCode);
    }

    private ResponseEntity<RestResponseDTO> buildResponseEntity(BaseResultCode resultCode, String... parameters) {

        HttpStatus httpStatus = HttpStatus.valueOf(resultCode.getCurrentHttpStatus());

        RestResponseDTO restResponse = new RestResponseDTO(resultCode, parameters);
        ResponseEntity<RestResponseDTO> responseEntity = new ResponseEntity<RestResponseDTO>(restResponse, httpStatus);
        return responseEntity;
    }

}