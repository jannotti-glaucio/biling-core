package tech.jannotti.billing.core.banking.santander.command;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBankChannel;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletRegistryRequest;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletRegistryResponse;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketRequest;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketResponse;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderDocumentType;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletRegistryRequestRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletRegistryResponseRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletTicketRequestRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletTicketResponseRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.DocumentTypeRepository;
import tech.jannotti.billing.core.banking.santander.soap.CobrancaSOAPClient;
import tech.jannotti.billing.core.banking.santander.soap.TicketSOAPClient;
import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.RegistraTitulo;
import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.RegistraTituloResponse;
import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.TituloGenericRequest;
import tech.jannotti.billing.core.banking.santander.soap.stub.cobranca.TituloGenericResponse;
import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.TicketRequest;
import tech.jannotti.billing.core.banking.santander.soap.stub.ticket.TicketResponse;
import tech.jannotti.billing.core.banking.santander.soap.translate.ResultCodeTranslator;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.command.AbstractConnectorCommand;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigitoSantander;

@Component("banking.santander.billetRegistryCommand")
public class BilletRegistryCommand extends AbstractConnectorCommand implements SantanderConstants {

    private static final int RET_CODE_SUCESSO = 0;

    private static final String SITUACAO_SUCESSO = "00";

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private BilletTicketRequestRepository billetTicketRequestRepository;

    @Autowired
    private BilletTicketResponseRepository billetTicketResponseRepository;

    @Autowired
    private BilletRegistryRequestRepository billetRegistryRequestRepository;

    @Autowired
    private BilletRegistryResponseRepository billetRegistryResponseRepository;

    @Autowired
    private TicketSOAPClient ticketSOAPClient;

    @Autowired
    private CobrancaSOAPClient cobrancaSOAPClient;

    @Autowired
    private ResultCodeTranslator resultCodeTranslator;

    private Random nossoNumeroPrefixRandom = new Random();

    public RegisterBankBilletConnectorResponse execute(SantanderBankChannel bankChannel,
        SantanderCompanyBankAccount companyBankAccount, BaseBankRemittance bankRemittance) {

        BasePaymentBankBillet bankBillet = bankRemittance.getBankBillet();
        long seuNumero = bankBillet.getId();

        // Grava a requisicao de ticket no banco
        SantanderBilletTicketRequest billetTicketRequest = buildBilletTicketRequest(companyBankAccount, bankRemittance,
            bankBillet,
            seuNumero);
        billetTicketRequestRepository.save(billetTicketRequest);

        long nossoNumero = billetTicketRequest.getTituloNossoNumero();

        // Cria o ticket de registro de titulo
        TicketRequest ticketRequest = buildTicketRequest(bankChannel, bankBillet, billetTicketRequest);
        TicketResponse ticketResponse = ticketSOAPClient.create(bankChannel.getTicketUrl(), ticketRequest,
            bankChannel.getRequestTimeout());

        // Grava a resposta de ticket no banco
        SantanderBilletTicketResponse billetTicketResponse = buildBilletTicketResponse(billetTicketRequest, ticketResponse);
        billetTicketResponseRepository.save(billetTicketResponse);

        // Valida o resultado da criacao do ticket
        if (billetTicketResponse.getRetCode() != RET_CODE_SUCESSO)
            return new RegisterBankBilletConnectorResponse(getGenericErrorResultCode());

        // Grava a requisicao de registro no banco
        SantanderBilletRegistryRequest billetRegistryRequest = buildBilletRegistryRequest(bankChannel, bankRemittance, bankBillet,
            seuNumero, billetTicketResponse);
        billetRegistryRequestRepository.save(billetRegistryRequest);

        // Confirma o registro do titulo
        RegistraTitulo registraTitulo = buildRegistraTitulo(companyBankAccount, billetRegistryRequest, ticketResponse);
        RegistraTituloResponse registraTituloResponse = cobrancaSOAPClient.registraTitulo(bankChannel.getCobrancaUrl(),
            registraTitulo, bankChannel.getRequestTimeout());

        // Grava a resposta de registro no banco
        SantanderBilletRegistryResponse billetRegistryResponse = buildBilletRegistryResponse(billetRegistryRequest,
            registraTituloResponse.getReturn());
        billetRegistryResponseRepository.save(billetRegistryResponse);

        // Valida o resultado da inclusao do titulo
        if (!registraTituloResponse.getReturn().getSituacao().equals(SITUACAO_SUCESSO)) {

            // Mapeia o codigo de erro
            BaseResultCode resultCode = resultCodeTranslator.translateDescricaoErroCodigoToResultCode(
                billetRegistryResponse.getDescricaoErroCodigo(), billetRegistryResponse.getDescricaoErroMensagem(), seuNumero);
            return new RegisterBankBilletConnectorResponse(resultCode);
        }

        return new RegisterBankBilletConnectorResponse(getSuccessResultCode(), nossoNumero, seuNumero,
            registraTituloResponse.getReturn().getTitulo().getLinDig());
    }

    private SantanderBilletTicketRequest buildBilletTicketRequest(SantanderCompanyBankAccount companyBankAccount,
        BaseBankRemittance bankRemittance, BasePaymentBankBillet bankBillet, Long seuNumero) {

        SantanderDocumentType payerDocumentType = documentTypeRepository
            .getByBaseDocumentType(bankBillet.getPayerDocumentType());

        long tituloNossoNumero = buildNossoNumero(seuNumero);

        SantanderBilletTicketRequest ticketRequest = new SantanderBilletTicketRequest();
        ticketRequest.setBankBillet(bankBillet);
        ticketRequest.setBaseBankRemittance(bankRemittance);
        ticketRequest.setRequestDate(DateTimeHelper.getNowDateTime());

        ticketRequest.setCodConvenio(companyBankAccount.getCodigoConvenioCobranca());
        ticketRequest.setPagadorTipoDocumento(payerDocumentType.getTipoDeDocumento());
        ticketRequest.setPagadorNumeroDocumento(bankBillet.getPayerDocumentNumber());
        ticketRequest.setTituloNossoNumero(tituloNossoNumero);
        ticketRequest.setTituloSeuNumero(seuNumero);
        ticketRequest.setTituloDataVencimento(bankBillet.getExpirationDate());
        ticketRequest.setTituloDataEmissao(bankBillet.getIssueDate());
        ticketRequest.setTituloEspecie(companyBankAccount.getEspecieTituloXml());
        ticketRequest.setTituloValorNominal(bankBillet.getAmount());

        // Multa e Juros
        if (bankBillet.isExpiredPayment()) {
            ticketRequest.setTituloPcMulta(bankBillet.getPenaltyPercent());

            Long penaltyDays = ChronoUnit.DAYS.between(bankBillet.getExpirationDate(), bankBillet.getPenaltyStartDate()) - 1;
            if (penaltyDays >= 0)
                ticketRequest.setTituloQtdDiasMulta(penaltyDays.intValue());

            ticketRequest.setTituloPcJuro(bankBillet.getInterestPercent()); // Juros Mensais
            ticketRequest.setTituloQtdDiasBaixa(0); // Baixa automatica conforme Convenio

        } else {
            ticketRequest.setTituloPcMulta(0); // Sem multa
            ticketRequest.setTituloPcJuro(0); // Isento de Juros
            ticketRequest.setTituloQtdDiasBaixa(1); // Baixa automatica apos Vencimento
        }

        return ticketRequest;
    }

    private SantanderBilletTicketResponse buildBilletTicketResponse(SantanderBilletTicketRequest billetTicketRequest,
        TicketResponse ticketResponse) {

        SantanderBilletTicketResponse billetTicketResponse = new SantanderBilletTicketResponse();

        billetTicketResponse.setBilletTicketRequest(billetTicketRequest);
        billetTicketResponse.setResponseDate(DateTimeHelper.getNowDateTime());
        billetTicketResponse.setRetCode(ticketResponse.getRetCode());
        billetTicketResponse.setMessage(ticketResponse.getMessage());

        return billetTicketResponse;
    }

    private SantanderBilletRegistryRequest buildBilletRegistryRequest(SantanderBankChannel bankChannel,
        BaseBankRemittance bankRemittance, BasePaymentBankBillet bankBillet, long seuNumero,
        SantanderBilletTicketResponse billetTicketResponse) {

        LocalDate dtNsu = DateTimeHelper.getNowDate();
        String nsu = buildNSU(seuNumero);

        SantanderBilletRegistryRequest registryRequest = new SantanderBilletRegistryRequest();
        registryRequest.setBankBillet(bankBillet);
        registryRequest.setBaseBankRemittance(bankRemittance);
        registryRequest.setBilletTicketResponse(billetTicketResponse);
        registryRequest.setRequestDate(DateTimeHelper.getNowDateTime());

        registryRequest.setDtNsu(dtNsu);
        registryRequest.setNsu(nsu);
        registryRequest.setTpAmbiente(bankChannel.getTipoAmbienteXml());

        return registryRequest;
    }

    private SantanderBilletRegistryResponse buildBilletRegistryResponse(SantanderBilletRegistryRequest billetRegistryRequest,
        TituloGenericResponse tituloGenericResponse) {

        SantanderBilletRegistryResponse billetRegistryResponse = new SantanderBilletRegistryResponse();

        billetRegistryResponse.setBilletRegistryRequest(billetRegistryRequest);
        billetRegistryResponse.setResponseDate(DateTimeHelper.getNowDateTime());
        billetRegistryResponse.setSituacao(Integer.valueOf(tituloGenericResponse.getSituacao()));
        billetRegistryResponse.setTituloLinDig(tituloGenericResponse.getTitulo().getLinDig());

        if (!tituloGenericResponse.getSituacao().equals(SITUACAO_SUCESSO))
            parseDescricaoErro(tituloGenericResponse.getDescricaoErro(), billetRegistryResponse);

        return billetRegistryResponse;
    }

    void parseDescricaoErro(String descricaoErro, SantanderBilletRegistryResponse billetRegistryResponse) {

        if (StringUtils.isBlank(descricaoErro))
            return;

        String lines[] = descricaoErro.split("\\r?\\n");
        if (lines.length >= 1) {
            String line = lines[0];

            if (line.length() >= 5) {
                String codigo = line.substring(0, 5);
                billetRegistryResponse.setDescricaoErroCodigo(codigo);
            }

            if (line.length() >= 6) {
                String mensagem = StringUtils.trim(line.substring(5));

                // Remove o separador -
                mensagem = StringUtils.removeStart(mensagem, "-");
                mensagem = StringUtils.trim(mensagem);

                billetRegistryResponse.setDescricaoErroMensagem(mensagem);

            }
        }
    }

    private TicketRequest buildTicketRequest(SantanderBankChannel bankChannel, BasePaymentBankBillet bankBillet,
        SantanderBilletTicketRequest billetTicketRequest) {

        TicketRequest.Dados dados = new TicketRequest.Dados();
        addEntry(dados, "CONVENIO.COD-BANCO", StringUtils.leftPad(SantanderConstants.BANK_NUMBER, 4, "0"));
        addEntry(dados, "CONVENIO.COD-CONVENIO", billetTicketRequest.getCodConvenio().toString());

        // Pagador
        addEntry(dados, "PAGADOR.TP-DOC",
            StringUtils.leftPad(billetTicketRequest.getPagadorTipoDocumento().toString(), 2, "0"));
        addEntry(dados, "PAGADOR.NUM-DOC", billetTicketRequest.getPagadorNumeroDocumento());

        addEntry(dados, "PAGADOR.NOME", normalize(bankBillet.getPayerName(), 40));
        addEntry(dados, "PAGADOR.ENDER", normalize(bankBillet.getPayerAddressFullStreet(), 40));
        addEntry(dados, "PAGADOR.BAIRRO", normalize(bankBillet.getPayerAddressDistrict(), 30));
        addEntry(dados, "PAGADOR.CIDADE", normalize(bankBillet.getPayerAddressCity(), 20));
        addEntry(dados, "PAGADOR.UF", bankBillet.getPayerAddressState());
        addEntry(dados, "PAGADOR.CEP", bankBillet.getPayerAddressZipCode());

        // Titulo
        addEntry(dados, "TITULO.NOSSO-NUMERO", billetTicketRequest.getTituloNossoNumero().toString());
        addEntry(dados, "TITULO.SEU-NUMERO", billetTicketRequest.getTituloSeuNumero().toString());
        addEntry(dados, "TITULO.DT-VENCTO",
            DateTimeHelper.format(billetTicketRequest.getTituloDataVencimento(), SOAP_DATE_FORMAT));
        addEntry(dados, "TITULO.DT-EMISSAO", DateTimeHelper.format(billetTicketRequest.getTituloDataEmissao(), SOAP_DATE_FORMAT));
        addEntry(dados, "TITULO.ESPECIE", StringUtils.leftPad(billetTicketRequest.getTituloEspecie().toString(), 2, "0"));
        addEntry(dados, "TITULO.VL-NOMINAL", billetTicketRequest.getTituloValorNominal().toString());
        addEntry(dados, "TITULO.TP-PAGAMENTO", "1"); // Soh aceita o pagamento integral

        // Multa e Juros
        addEntry(dados, "TITULO.PC-MULTA", billetTicketRequest.getTituloPcMulta().toString());
        if (billetTicketRequest.getTituloQtdDiasMulta() != null)
            addEntry(dados, "TITULO.QT-DIAS-MULTA", billetTicketRequest.getTituloQtdDiasMulta().toString());
        addEntry(dados, "TITULO.PC-JURO", billetTicketRequest.getTituloPcJuro().toString());
        addEntry(dados, "TITULO.QT-DIAS-BAIXA", billetTicketRequest.getTituloQtdDiasBaixa().toString());

        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setDados(dados);
        ticketRequest.setExpiracao(bankChannel.getExpiracaoTicket());
        ticketRequest.setSistema("YMB");

        return ticketRequest;
    }

    private RegistraTitulo buildRegistraTitulo(SantanderCompanyBankAccount companyBankAccount,
        SantanderBilletRegistryRequest billetRegistryRequest, TicketResponse ticketResponse) {

        TituloGenericRequest dto = new TituloGenericRequest();
        dto.setDtNsu(DateTimeHelper.format(billetRegistryRequest.getDtNsu(), SOAP_DATE_FORMAT));
        dto.setNsu(billetRegistryRequest.getNsu());
        dto.setEstacao(companyBankAccount.getCodigoEstacaoCobranca());
        dto.setTicket(ticketResponse.getTicket());
        dto.setTpAmbiente(billetRegistryRequest.getTpAmbiente());

        RegistraTitulo registraTitulo = new RegistraTitulo();
        registraTitulo.setDto(dto);

        return registraTitulo;
    }

    private void addEntry(TicketRequest.Dados dados, String key, String value) {

        TicketRequest.Dados.Entry entry = new TicketRequest.Dados.Entry();
        entry.setKey(key);
        entry.setValue(value);
        dados.getEntry().add(entry);
    }

    private long buildNossoNumero(Long seuNumero) {
        Long nossoNumero = null;

        if (springContextHelper.isOnTestsProfile()) {
            Integer nossoNumeroPrefix = nossoNumeroPrefixRandom.nextInt(999);
            nossoNumero = Long.valueOf(nossoNumeroPrefix.toString() + seuNumero.toString());

        } else
            nossoNumero = seuNumero;

        GeradorDeDigitoSantander geradorDeDigito = new GeradorDeDigitoSantander();
        String dvNossoNumero = geradorDeDigito.calculaDVNossoNumero(nossoNumero.toString());

        return Long.valueOf(nossoNumero.toString() + dvNossoNumero);
    }

    private String buildNSU(Long seuNumero) {

        if (springContextHelper.isOnTestsProfile())
            return "TST" + StringUtils.leftPad(seuNumero.toString(), 17, "0");
        else
            return StringUtils.leftPad(seuNumero.toString(), 20, "0");
    }

}
