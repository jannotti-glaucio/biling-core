package tech.jannotti.billing.core.services.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.repository.base.transfer.TransferTypeRepository;
import tech.jannotti.billing.core.services.AbstractService;

@Service
public abstract class AbstractTransferService extends AbstractService {

    @Autowired
    protected TransferTypeRepository transferTypeRepository;

    protected String generateToken() {
        return tokenGenerator.generateRandomHexToken("transfer.token", 20);
    }

}
