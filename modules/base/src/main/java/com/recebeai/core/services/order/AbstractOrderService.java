package tech.jannotti.billing.core.services.order;

import tech.jannotti.billing.core.services.AbstractService;

public abstract class AbstractOrderService extends AbstractService {

    protected String generateToken() {
        return tokenGenerator.generateRandomHexToken("order.token", 22);
    }

}
