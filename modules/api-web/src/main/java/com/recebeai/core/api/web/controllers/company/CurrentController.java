package tech.jannotti.billing.core.api.web.controllers.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.bankaccount.FindBankAccountsRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.services.company.CompanyService;

@RestController("company.currentController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/current")
public class CurrentController extends AbstractWebController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/bankAccounts")
    @InfoLogging
    public FindBankAccountsRestResponse getBankAccounts() {
        BaseCompany company = getLoggedCompany();

        List<BaseCompanyBankAccount> accounts = companyService.getBankAccounts(company);
        return new FindBankAccountsRestResponse(getSuccessResultCode(), dtoMapper, accounts);
    }

}
