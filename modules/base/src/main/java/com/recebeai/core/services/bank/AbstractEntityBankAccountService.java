package tech.jannotti.billing.core.services.bank;

import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.services.AbstractService;

@Service
public class AbstractEntityBankAccountService extends AbstractService {

    protected String generateToken() {
        return tokenGenerator.generateRandomHexToken("bankAccount.token", 14);
    }

}
