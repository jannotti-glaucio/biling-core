package tech.jannotti.billing.core.banking.bb.command;

import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryRequest;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BilletRegistryRequestRepository;
import tech.jannotti.billing.core.banking.exception.BankingException;
import tech.jannotti.billing.core.banking.stella.AbstractStellaBankingCommand;
import tech.jannotti.billing.core.banking.stella.bancos.BancoDoBrasilV2;
import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;

@Component("banking.bb.billetPrintCommand")
public class BilletPrintCommand extends AbstractStellaBankingCommand implements BancoBrasilConstants {

    private static final String REPORT_PATH = "/banking/bb/jasper/boleto.jasper";

    @Autowired
    private BilletRegistryRequestRepository billetRegistryRequestRepository;

    public PrintBankBilletConnectorResponse execute(BancoBrasilCompanyBankAccount companyBankAccount,
        BasePaymentBankBillet bankBillet) {

        BancoBrasilBilletRegistryRequest registryRequest = billetRegistryRequestRepository.getByBankBillet(bankBillet);
        if (registryRequest == null)
            throw new BankingException("Nao foi localizada a requisicao de registro desse boleto no BB [bankBilletId=%s]",
                bankBillet.getId());

        BaseDealer dealer = bankBillet.getInvoice().getCustomer().getDealer();
        BaseBank bank = bankBillet.getCompanyBankAccount().getBank();
        StrSubstitutor templateSubstitutor = buildTemplateSubstitutor(dealer);

        Banco bancoBrasil = new BancoDoBrasilV2();
        Boleto boleto = buildBoleto(bancoBrasil, bankBillet, registryRequest.getNumeroCarteira().toString(),
            companyBankAccount.getEspecieTituloBoleto(), templateSubstitutor);

        boleto.getBeneficiario().comNumeroConvenio(registryRequest.getNumeroConvenio().toString());

        Map<String, Object> parameters = initReportParameters();

        // TODO Quando customizarmos o objeto Boleto jogar esses propriedades pra dentro dele
        String demonstratives = buildDemonstratives(bank, templateSubstitutor);
        parameters.put("demonstrativo", demonstratives);

        parameters.put("descricao", bankBillet.getInvoice().getDescription());

        String instructions = buildInstructions(bank, dealer, templateSubstitutor, LINE_BREAK, bankBillet.isExpiredPayment());
        parameters.put("instrucoes", instructions);

        byte[] billetContent = buildPDF(boleto, REPORT_PATH, parameters);
        return new PrintBankBilletConnectorResponse(getSuccessResultCode(), billetContent);

    }

}
