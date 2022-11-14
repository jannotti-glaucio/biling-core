package tech.jannotti.billing.core.banking.santander.command;

import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.exception.BankingException;
import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketRequest;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletTicketRequestRepository;
import tech.jannotti.billing.core.banking.stella.AbstractStellaBankingCommand;
import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.bancos.Santander;

@Component("banking.santander.billetPrintCommand")
public class BilletPrintCommand extends AbstractStellaBankingCommand implements SantanderConstants {

    private static final String REPORT_PATH = "/banking/santander/jasper/boleto.jasper";

    @Autowired
    private BilletTicketRequestRepository billetTicketRequestRepository;

    public PrintBankBilletConnectorResponse execute(SantanderCompanyBankAccount companyBankAccount,
        BasePaymentBankBillet bankBillet) {

        SantanderBilletTicketRequest ticketRequest = billetTicketRequestRepository.getByBankBillet(bankBillet);
        if (ticketRequest == null)
            throw new BankingException("Nao foi localizada a requisicao de registro desse boleto no Santander [bankBilletId=%s]",
                bankBillet.getId());

        BaseDealer dealer = bankBillet.getInvoice().getCustomer().getDealer();
        BaseBank bank = bankBillet.getCompanyBankAccount().getBank();
        StrSubstitutor templateSubstitutor = buildTemplateSubstitutor(dealer);

        Banco santander = new Santander();
        Boleto boleto = buildBoleto(santander, bankBillet, companyBankAccount.getCodigoCarteiraBoleto().toString(),
            companyBankAccount.getEspecieTituloBoleto(), templateSubstitutor);

        boleto.getBeneficiario().comNumeroConvenio(ticketRequest.getCodConvenio().toString());

        Map<String, Object> parameters = initReportParameters();

        parameters.put("carteira", companyBankAccount.getSiglaCarteiraBoleto());

        String demonstratives = buildDemonstratives(bank, templateSubstitutor);
        parameters.put("demonstrativo", demonstratives);

        parameters.put("descricao", bankBillet.getInvoice().getDescription());

        String instructions = buildInstructions(bank, dealer, templateSubstitutor, LINE_BREAK, bankBillet.isExpiredPayment());
        parameters.put("instrucoes", instructions);

        byte[] billetContent = buildPDF(boleto, REPORT_PATH, parameters);
        return new PrintBankBilletConnectorResponse(getSuccessResultCode(), billetContent);
    }

}
