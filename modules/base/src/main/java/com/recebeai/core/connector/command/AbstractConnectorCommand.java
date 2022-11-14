package tech.jannotti.billing.core.connector.command;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.bean.NormalizerHelper;
import tech.jannotti.billing.core.commons.spring.SpringContextHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.services.ResultCodeService;

public abstract class AbstractConnectorCommand {

    @Autowired
    protected SpringContextHelper springContextHelper;

    @Autowired
    protected ResultCodeService resultCodeService;

    protected BaseResultCode getSuccessResultCode() {
        return resultCodeService.getSuccessResultCode();
    }

    protected BaseResultCode getGenericErrorResultCode() {
        return resultCodeService.getGenericErrorResultCode();
    }

    protected BaseResultCode getResultCode(String resultCodeKey) {
        return resultCodeService.getByKey(resultCodeKey);
    }

    protected String normalize(String value, int size) {
        return NormalizerHelper.normalize(value, size).toUpperCase();
    }

}
