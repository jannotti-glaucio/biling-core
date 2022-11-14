package tech.jannotti.billing.core.banking.stella;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import tech.jannotti.billing.core.banking.util.BilletOurNumberHelper;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.commons.util.NumberHelper;
import tech.jannotti.billing.core.connector.banking.command.AbstractBankingCommand;
import tech.jannotti.billing.core.connector.exception.ConnectorException;
import tech.jannotti.billing.core.persistence.model.AbstractAddress;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.exception.CriacaoBoletoException;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;

public abstract class AbstractStellaBankingCommand extends AbstractBankingCommand {

    protected static final String LINE_BREAK = "<br/>";

    protected Map<String, Object> initReportParameters() {
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        return reportParameters;
    }

    protected Boleto buildBoleto(Banco banco, BasePaymentBankBillet bankBillet, String wallet, String documentKind,
        StrSubstitutor templateSubstitutor) {

        Calendar documentDateCalendar = DateTimeHelper.toCalendar(bankBillet.getIssueDate());
        Calendar expirationDateCalendar = DateTimeHelper.toCalendar(bankBillet.getExpirationDate());

        Datas datas = Datas.novasDatas()
            .comDocumento(documentDateCalendar)
            .comProcessamento(documentDateCalendar)
            .comVencimento(expirationDateCalendar);

        BaseDealer dealer = bankBillet.getInvoice().getCustomer().getDealer();
        AbstractAddress beneficiaryAddress = bankingService.getBeneficiaryAddress(dealer);

        BaseCompanyBankAccount companyBankAccount = bankBillet.getCompanyBankAccount();
        BaseBank bank = companyBankAccount.getBank();
        String beneficiaryName = buildBeneficiaryName(bank, templateSubstitutor);

        BaseDocumentType documentType = dealer.getDocumentType();
        String beneficiaryDocumentNumber = documentType.format(dealer.getDocumentNumber());

        String beneficiaryStreet = beneficiaryAddress.getFullStreet();
        String beneficiaryDistrict = beneficiaryAddress.getDistrict();
        String beneficiaryZipCode = beneficiaryAddress.getZipCode();
        String beneficiaryCity = beneficiaryAddress.getCity();
        String beneficiaryState = beneficiaryAddress.getState();

        String paymentPlace = buildPaymentPlace(bank, templateSubstitutor, bankBillet.isExpiredPayment());

        // Beneficiario
        Endereco enderecoBeneficiario = Endereco.novoEndereco()
            .comLogradouro(beneficiaryStreet)
            .comBairro(beneficiaryDistrict)
            .comCep(beneficiaryZipCode)
            .comCidade(beneficiaryCity)
            .comUf(beneficiaryState);

        String agencyCheckDigit = ((companyBankAccount.getAgencyCheckDigit() != null)
            ? companyBankAccount.getAgencyCheckDigit().toString()
            : null);

        String accountCheckDigit = ((companyBankAccount.getAccountCheckDigit() != null)
            ? companyBankAccount.getAccountCheckDigit().toString()
            : null);

        String nossoNumero = BilletOurNumberHelper.formatOutNumberToPrinting(bankBillet);

        Beneficiario beneficiario = Beneficiario.novoBeneficiario()
            .comNomeBeneficiario(beneficiaryName)
            .comDocumento(beneficiaryDocumentNumber)
            .comAgencia(companyBankAccount.getAgencyNumber()).comDigitoAgencia(agencyCheckDigit)
            .comCodigoBeneficiario(companyBankAccount.getAccountNumber()).comDigitoCodigoBeneficiario(accountCheckDigit)
            .comCarteira(wallet)
            .comNossoNumero(nossoNumero)
            .comEndereco(enderecoBeneficiario);

        Integer ourNumberDigits = BilletOurNumberHelper.getOurNumberDigits(bankBillet);
        if (ourNumberDigits != null)
            beneficiario.comDigitoNossoNumero(ourNumberDigits.toString());

        // Pagador
        Endereco enderecoPagador = Endereco.novoEndereco()
            .comLogradouro(bankBillet.getPayerAddressFullStreet())
            .comBairro(bankBillet.getPayerAddressDistrict())
            .comCep(bankBillet.getPayerAddressZipCode())
            .comCidade(bankBillet.getPayerAddressCity())
            .comUf(bankBillet.getPayerAddressState());

        Pagador pagador = Pagador.novoPagador()
            .comNome(bankBillet.getPayerName())
            .comDocumento(bankBillet.getPayerDocumentNumber())
            .comEndereco(enderecoPagador);

        double amountDecimal = NumberHelper.fromIntegerToDouble(bankBillet.getAmount());

        // Boleto
        Boleto boleto = Boleto.novoBoleto()
            .comBanco(banco)
            .comDatas(datas)
            .comBeneficiario(beneficiario)
            .comPagador(pagador)
            .comValorBoleto(amountDecimal)
            .comAceite(companyBankAccount.isAcceptance())
            .comEspecieDocumento(documentKind)
            .comLocaisDePagamento(paymentPlace);

        if (bankBillet.getDocumentNumber() != null)
            boleto.comNumeroDoDocumento(bankBillet.getDocumentNumber().toString());

        return boleto;
    }

    private GeradorDeBoleto buildGeradorDeBoleto(Boleto boleto, String reportPath, Map<String, Object> parameters) {

        InputStream reportStream = null;
        if (StringUtils.isNotBlank(reportPath))
            reportStream = this.getClass().getResourceAsStream(reportPath);

        GeradorDeBoleto gerador = new GeradorDeBoleto(reportStream, parameters, boleto);
        return gerador;
    }

    protected byte[] buildPDF(Boleto boleto, String reportPath, Map<String, Object> parameters) {

        GeradorDeBoleto geradorDeBoleto = buildGeradorDeBoleto(boleto, reportPath, parameters);
        return geradorDeBoleto.geraPDF();
    }

    protected String getLinhaDigitavel(Boleto boleto) {
        try {
            return boleto.getLinhaDigitavel();

        } catch (CriacaoBoletoException e) {
            throw new ConnectorException("Erro gerando linha digitavel", e);
        }
    }

}
