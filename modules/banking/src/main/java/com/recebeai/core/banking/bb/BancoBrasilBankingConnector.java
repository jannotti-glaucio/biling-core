package tech.jannotti.billing.core.banking.bb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.command.BilletPrintCommand;
import tech.jannotti.billing.core.banking.bb.command.BilletRegistryCommand;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBankChannel;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BankChannelRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.connector.banking.AbstractBankingConnector;
import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.exception.ConnectorException;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

@Component("bankingConnectors.bb")
public class BancoBrasilBankingConnector extends AbstractBankingConnector {

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private BankChannelRepository bankChannelRepository;

    @Autowired
    private BilletRegistryCommand billetRegistryCommand;

    @Autowired
    private BilletPrintCommand billetPrintCommand;

    public RegisterBankBilletConnectorResponse registerBankBillet(BaseBankRemittance bankRemittance) {

        BasePaymentBankBillet bankBillet = bankRemittance.getBankBillet();
        BaseCompanyBankAccount baseCompanyBankAccount = bankBillet.getCompanyBankAccount();

        BancoBrasilBankChannel bankChannel = bankChannelRepository.getByBaseBankChannel(baseCompanyBankAccount.getBankChannel());
        if (bankChannel == null)
            throw new ConnectorException("Canal bancario nao configurado no schema do Banco do Brasil [code=%s]",
                baseCompanyBankAccount.getBankChannel().getCode());

        BancoBrasilCompanyBankAccount companyBankAccount = companyBankAccountRepository.getByBaseCompanyBankAccount(baseCompanyBankAccount);
        if (companyBankAccount == null)
            throw new ConnectorException("Conta nao configurada no schema do Banco do Brasil [token=%s]",
                baseCompanyBankAccount.getToken());

        RegisterBankBilletConnectorResponse response = billetRegistryCommand.execute(bankChannel, companyBankAccount, bankRemittance);
        return response;
    }

    public PrintBankBilletConnectorResponse printBankBillet(BasePaymentBankBillet bankBillet) {

        BancoBrasilCompanyBankAccount companyBankAccount = companyBankAccountRepository
            .getByBaseCompanyBankAccount(bankBillet.getCompanyBankAccount());
        if (companyBankAccount == null)
            throw new ConnectorException("Conta nao configurada no schema do Banco do Brasil [token=%s]",
                bankBillet.getCompanyBankAccount().getToken());

        PrintBankBilletConnectorResponse response = billetPrintCommand.execute(companyBankAccount, bankBillet);
        return response;
    }

}
