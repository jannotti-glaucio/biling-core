package tech.jannotti.billing.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.repository.base.ResultCodeRepository;

@Service
public class ResultCodeService extends AbstractService {

    @Autowired
    private ResultCodeRepository resulCodeRepository;

    public BaseResultCode getByKey(String key) {
        return resulCodeRepository.getByKey(key);
    }

    public BaseResultCode getSuccessResultCode() {
        return getByKey(ResultCodeConstants.CODE_SUCCESS);
    }

    public BaseResultCode getGenericErrorResultCode() {
        return getByKey(ResultCodeConstants.CODE_GENERIC_ERROR);
    }

}
