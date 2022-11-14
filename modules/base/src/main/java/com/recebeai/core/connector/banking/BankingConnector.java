package tech.jannotti.billing.core.connector.banking;

import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

public interface BankingConnector {

    public RegisterBankBilletConnectorResponse registerBankBillet(BaseBankRemittance bankRemittance);

    public PrintBankBilletConnectorResponse printBankBillet(BasePaymentBankBillet bankBillet);

}
