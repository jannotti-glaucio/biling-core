package tech.jannotti.billing.core.connector.banking;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.exception.ConnectorException;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("bankingConnectors.mock")
public class MockBankingConnector extends AbstractBankingConnector {

    private static final String LINE_CODE = "34190.30006 00000.140723 00989.870001 5 67490000010000";

    private static final String BANK_BILLET_PATH = "/banking/mock-bank-billet.pdf";

    @Override
    public RegisterBankBilletConnectorResponse registerBankBillet(BaseBankRemittance bankRemittance) {
        BaseResultCode resultCode = getSuccessResultCode();

        return new RegisterBankBilletConnectorResponse(resultCode, bankRemittance.getBankBillet().getId(),
            bankRemittance.getBankBillet().getId(), LINE_CODE);
    }

    @Override
    public PrintBankBilletConnectorResponse printBankBillet(BasePaymentBankBillet bankBillet) {

        InputStream inputStream = MockBankingConnector.class.getResourceAsStream(BANK_BILLET_PATH);
        byte[] billetContent;
        try {
            billetContent = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ConnectorException("Erro lendo boleto de mock", e);
        }

        BaseResultCode resultCode = getSuccessResultCode();
        return new PrintBankBilletConnectorResponse(resultCode, billetContent);
    }

}
