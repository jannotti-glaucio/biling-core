package tech.jannotti.billing.core.services.market;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.services.AbstractService;

public abstract class AbstractEntityMarketService extends AbstractService {

    @Autowired
    protected MarketAccountService marketAccountService;

    @Autowired
    protected MarketStatementService marketStatementService;

    @Autowired
    protected MarketWithdrawService marketWithdrawService;

    protected String generateToken() {
        return tokenGenerator.generateRandomHexToken("marketAccount.token", 14);
    }

}
