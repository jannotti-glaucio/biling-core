package tech.jannotti.billing.core.api.web.controllers.dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.bankaccount.FindBankAccountsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.GetMarketBalanceRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.services.dealer.DealerMarketService;
import tech.jannotti.billing.core.services.dealer.DealerService;

@RestController("dealer.currentController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/current")
public class CurrentController extends AbstractWebController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerMarketService dealerMarketService;

    @GetMapping("/bankAccounts")
    @InfoLogging
    public FindBankAccountsRestResponse getBankAccount() {
        BaseDealer dealer = getLoggedDealer();

        List<BaseDealerBankAccount> accounts = dealerService.getBankAccounts(dealer);
        return new FindBankAccountsRestResponse(getSuccessResultCode(), dtoMapper, accounts);
    }

    @GetMapping("/market/balance")
    @InfoLogging
    public GetMarketBalanceRestResponse getCurrentBalance() {
        BaseDealer dealer = getLoggedDealer();

        long currentBalance = dealerMarketService.getCurrentBalance(dealer);
        return new GetMarketBalanceRestResponse(getSuccessResultCode(), currentBalance);
    }

}
