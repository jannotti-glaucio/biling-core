package tech.jannotti.billing.core.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.commons.security.TokenGenerator;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.services.dto.converter.ServiceDTOMapper;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.UpdateBankAccountServiceRequest;

public abstract class AbstractService implements ResultCodeConstants {

    @Autowired
    protected ServiceDTOMapper dtoMapper;

    @Autowired
    protected TokenGenerator tokenGenerator;

    @Autowired
    private ResultCodeService resultCodeService;

    protected BaseResultCode getResultCode(String key) {
        return resultCodeService.getByKey(key);
    }

    protected BaseResultCode getSuccessResultCode() {
        return resultCodeService.getSuccessResultCode();
    }

    protected BaseResultCode getGenericErrorResultCode() {
        return resultCodeService.getGenericErrorResultCode();
    }

    protected List<UpdateAddressServiceRequest> filterUpdateAddressRequests(List<UpdateAddressServiceRequest> requests,
        CRUDOperationEnum operation) {

        if (CollectionUtils.isEmpty(requests))
            return null;

        List<UpdateAddressServiceRequest> value = requests.stream()
            .filter(x -> x.getOperation().equals(operation))
            .collect(Collectors.toList());
        return value;
    }

    protected List<UpdateBankAccountServiceRequest> filterUpdateBankAccountRequests(
        List<UpdateBankAccountServiceRequest> requests, CRUDOperationEnum operation) {

        if (CollectionUtils.isEmpty(requests))
            return null;

        List<UpdateBankAccountServiceRequest> value = requests.stream()
            .filter(x -> x.getOperation().equals(operation))
            .collect(Collectors.toList());
        return value;
    }

}
