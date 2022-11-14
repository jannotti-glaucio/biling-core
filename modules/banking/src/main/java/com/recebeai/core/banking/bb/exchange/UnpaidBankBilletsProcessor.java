package tech.jannotti.billing.core.banking.bb.exchange;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.exchange.AbstractUnpaidBankBilletsProcessor;

@Component("banking.bb.unpaidBankBilletsProcessor")
public class UnpaidBankBilletsProcessor extends AbstractUnpaidBankBilletsProcessor {

    @Override
    protected String getBankNumber() {
        return BancoBrasilConstants.BANK_NUMBER;
    }

}
