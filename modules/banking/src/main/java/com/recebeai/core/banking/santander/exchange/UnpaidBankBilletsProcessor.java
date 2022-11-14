package tech.jannotti.billing.core.banking.santander.exchange;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.exchange.AbstractUnpaidBankBilletsProcessor;
import tech.jannotti.billing.core.banking.santander.SantanderConstants;

@Component("banking.santander.unpaidBankBilletsProcessor")
public class UnpaidBankBilletsProcessor extends AbstractUnpaidBankBilletsProcessor {

    @Override
    protected String getBankNumber() {
        return SantanderConstants.BANK_NUMBER;
    }

}
