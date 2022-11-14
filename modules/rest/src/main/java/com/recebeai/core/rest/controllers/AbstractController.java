package tech.jannotti.billing.core.rest.controllers;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import tech.jannotti.billing.core.commons.util.NumberHelper;
import tech.jannotti.billing.core.constants.CoreConstants;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.rest.security.util.SecurityHelper;
import tech.jannotti.billing.core.services.ResultCodeService;

@CrossOrigin
public abstract class AbstractController implements ResultCodeConstants, ApiConstants {

    @Autowired
    private ResultCodeService resultCodeService;

    @Autowired
    protected SecurityHelper securityHelper;

    protected <T> ResponseEntity<T> buildOkResponseEntity(T responseBody, HttpHeaders headers) {
        ResponseEntity<T> responseEntity = new ResponseEntity<T>(responseBody, headers, HttpStatus.OK);
        return responseEntity;
    }

    protected <T> ResponseEntity<T> buildResponseEntity(T responseBody, HttpHeaders headers, HttpStatus httpStatus) {
        ResponseEntity<T> responseEntity = new ResponseEntity<T>(responseBody, headers, httpStatus);
        return responseEntity;
    }

    protected BaseResultCode getResultCode(String key) {
        return resultCodeService.getByKey(key);
    }

    protected BaseResultCode getSuccessResultCode() {
        return resultCodeService.getSuccessResultCode();
    }

    protected BaseResultCode getQueryResultCode(List<? extends AbstractModel> list) {
        if (!CollectionUtils.isEmpty(list))
            return getSuccessResultCode();
        else
            return getResultCode(CODE_EMPTY_QUERY_RESULT);
    }

    protected BaseResultCode getQueryResultCode(AbstractModel model) {
        if (model != null)
            return getSuccessResultCode();
        else
            return getResultCode(CODE_EMPTY_QUERY_RESULT);
    }

    protected RestResponseDTO createResponse(BaseResultCode resultCode, String... parameters) {
        RestResponseDTO response = new RestResponseDTO(resultCode, parameters);
        return response;
    }

    protected RestResponseDTO createResponse(String resultCodeKey, String... parameters) {
        BaseResultCode resultCode = resultCodeService.getByKey(resultCodeKey);
        return createResponse(resultCode, parameters);
    }

    protected RestResponseDTO createSuccessResponse() {
        BaseResultCode resultCode = resultCodeService.getSuccessResultCode();
        return createResponse(resultCode);
    }

    // TODO Mover esse metodo para uma classe helper do core
    protected PageRequest createPageRequest(String pageParam, String sizeParam, String sortParam, String directionParam) {

        // Não quer paginar e nem ordenar
        if (StringUtils.isBlank(pageParam) && StringUtils.isBlank(sizeParam) && (StringUtils.isBlank(sortParam)))
            return null;

        // Se enviar page ou size sozinho, sem enviar os 2 juntos
        if ((StringUtils.isBlank(pageParam) & StringUtils.isNotBlank(sizeParam))
            || ((StringUtils.isNotBlank(pageParam) & StringUtils.isBlank(sizeParam))))
            throw new ResultCodeControllerException(CODE_INVALID_PAGGING_PARAMETERS, pageParam, sizeParam);

        // Se enviar direction sozinho, sem enviar o sort
        if ((StringUtils.isBlank(sortParam) & StringUtils.isNotBlank(directionParam)))
            throw new ResultCodeControllerException(CODE_INVALID_SORTING_PARAMETERS, sortParam, directionParam);

        Integer page = NumberHelper.parseAsInteger(pageParam);
        if (page == null)
            page = CoreConstants.DEFAULT_PAGGING_PAGE;

        Integer size = NumberHelper.parseAsInteger(sizeParam);
        if (size == null)
            size = CoreConstants.PAGGING_SIZE_LIMIT;

        // Se o tamanho da pagina for acima do limite
        if (size > CoreConstants.PAGGING_SIZE_LIMIT)
            throw new ResultCodeControllerException(CODE_PAGGING_SIZE_EXCEEDS_LIMIT, sizeParam);

        Sort.Direction direction = Sort.Direction.fromStringOrNull(directionParam);
        Sort sort = parseSort(sortParam, direction);

        PageRequest pageRequest = null;
        if (sort == null)
            pageRequest = new PageRequest(page, size);
        else
            pageRequest = new PageRequest(page, size, sort);

        return pageRequest;
    }

    protected PageRequest createPageRequest(String pageParam, String sizeParam) {
        return createPageRequest(pageParam, sizeParam, null, null);
    }

    private Sort parseSort(String sortParam, Direction direction) {

        if (StringUtils.isBlank(sortParam))
            return null;

        Sort.Order order = null;
        if (direction == null)
            order = new Sort.Order(Sort.Direction.ASC, sortParam);
        else
            order = new Sort.Order(direction, sortParam);

        Sort sort = new Sort(order);
        return sort;
    }

}
