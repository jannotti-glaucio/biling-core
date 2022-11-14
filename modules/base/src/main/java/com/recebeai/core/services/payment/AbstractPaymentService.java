package tech.jannotti.billing.core.services.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.repository.base.payment.PaymentRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.customer.CustomerService;

@Service
public abstract class AbstractPaymentService extends AbstractService {

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected CustomerService customerService;

    protected String generateToken() {
        return tokenGenerator.generateRandomHexToken("payment.token", 24);
    }

}
