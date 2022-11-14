package tech.jannotti.billing.core.connector.response;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.services.ResultCodeService;

public abstract class AbstractResultCodeTranslator {

    @Autowired
    protected ResultCodeService resultCodeService;

    protected BaseResultCode getAcquirerUnknowReturnResultCode() {
        BaseResultCode resultCode = resultCodeService.getByKey(ResultCodeConstants.CODE_ACQUIRER_UNKNOW_RETURN);
        return resultCode;
    }

    protected BaseResultCode getBankUnknowReturnResultCode() {
        BaseResultCode resultCode = resultCodeService.getByKey(ResultCodeConstants.CODE_BANKING_UNKNOW_RETURN);
        return resultCode;
    }

}
