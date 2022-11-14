package tech.jannotti.billing.core.api.web.controllers.shared;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.banking.FindBankRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.services.bank.BankingService;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "banking")
public class BankingController extends AbstractWebController {

    @Autowired
    private BankingService bankingService;

    @GetMapping("/bank")
    @InfoLogging
    public FindBankRestResponse find() {

        List<BaseBank> banks = bankingService.getBanks();
        return new FindBankRestResponse(getSuccessResultCode(), dtoMapper, banks);
    }

}
