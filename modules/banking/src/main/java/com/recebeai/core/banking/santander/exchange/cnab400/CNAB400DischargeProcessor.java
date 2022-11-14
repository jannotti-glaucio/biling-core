package tech.jannotti.billing.core.banking.santander.exchange.cnab400;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.exchange.AbstractBankDischargeProcessor;
import tech.jannotti.billing.core.banking.santander.SantanderConstants;
import tech.jannotti.billing.core.banking.santander.exchange.ResultCodeTranslator;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketRequest;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCompanyBankAccount;
import tech.jannotti.billing.core.banking.santander.persistence.repository.BilletTicketRequestRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400DischargeDetailRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400DischargeFileRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.santander.cnab400DischargeProcessor")
public class CNAB400DischargeProcessor extends AbstractBankDischargeProcessor {

    private static final LogManager logManager = LogFactory.getManager(CNAB400DischargeProcessor.class);

    private static final Integer CODIGO_DE_OCORRENCIA_ENTRADA_CONFIRMADA = 2;
    private static final Integer CODIGO_DE_OCORRENCIA_ENTRADA_REJEITADA = 3;
    private static final Integer CODIGO_DE_OCORRENCIA_LIQUIDACAO = 6;
    private static final Integer CODIGO_DE_OCORRENCIA_BAIXA_AUTOMATICA = 9;
    private static final Integer CODIGO_DE_OCORRENCIA_BAIXA_POR_INSTRUCAO = 10;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private CNAB400DischargeFileRepository dischargeFileRepository;

    @Autowired
    private CNAB400DischargeDetailRepository dischargeDetailRepository;

    @Autowired
    private BilletTicketRequestRepository billetTicketRequestRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    @Autowired
    private ResultCodeTranslator resultCodeTranslator;

    @Override
    protected String getBankNumber() {
        return SantanderConstants.BANK_NUMBER;
    }

    @Override
    public void processDischarges(BaseCompanyBankAccount baseCompanyBankAccount) {

        SantanderCompanyBankAccount companyBankAccount = companyBankAccountRepository
            .getByBaseCompanyBankAccount(baseCompanyBankAccount);

        List<SantanderCNAB400DischargeFile> dischargeFiles = dischargeFileRepository.findByCompanyBankAccountAndDischargeStatus(
            companyBankAccount, BankDischargeStatusEnum.PENDING);

        if (CollectionUtils.isEmpty(dischargeFiles)) {
            logManager.logDEBUG("Nao ha arquivos para processar");
            return;
        }

        for (SantanderCNAB400DischargeFile dischargeFile : dischargeFiles) {
            logManager.logINFO("Processando Arquivo de Retorno [id=%s]", dischargeFile.getId());

            List<SantanderCNAB400DischargeDetail> dischargeDetails = dischargeDetailRepository
                .findByDischargeFileAndStatus(dischargeFile, BankDischargeStatusEnum.PENDING);

            if (dischargeDetails == null) {
                logManager.logDEBUG("Arquivo nao tem retornos bancarios para processar");
                continue;
            }

            for (SantanderCNAB400DischargeDetail dischargeDetail : dischargeDetails) {
                logManager.logINFO("Processando Retorno Bancario [id=%s]", dischargeDetail.getId());

                LocalDateTime processingDate = DateTimeHelper.getNowDateTime();

                BasePaymentBankBillet baseBankBillet = findBankBillet(baseCompanyBankAccount, dischargeDetail);

                // Rejeitando registro de boleto nao localizado
                if (baseBankBillet == null) {
                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.DENIED, processingDate);
                    logManager.logWARN(
                        "Rejeitando retorno pois o boleto nao foi localizado [nossoNumero=%s, codigoDeOcorrencia=%s, codigoDoErro=%s]",
                        dischargeDetail.getNossoNumero(), dischargeDetail.getCodigoDeOcorrencia(),
                        dischargeDetail.getCodigoDoPrimeiroErro());
                    continue;
                }

                if (dischargeDetail.getCodigoDeOcorrencia().equals(CODIGO_DE_OCORRENCIA_ENTRADA_CONFIRMADA)) {
                    // Registro
                    processRegistration(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getCodigoDeOcorrencia().equals(CODIGO_DE_OCORRENCIA_LIQUIDACAO)) {
                    // Pagamento
                    processPayment(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getCodigoDeOcorrencia().equals(CODIGO_DE_OCORRENCIA_BAIXA_POR_INSTRUCAO)) {
                    // Cancelamento
                    processCancellation(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getCodigoDeOcorrencia().equals(CODIGO_DE_OCORRENCIA_BAIXA_AUTOMATICA)) {
                    // Baixa Automatica
                    processPurge(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getCodigoDeOcorrencia().equals(CODIGO_DE_OCORRENCIA_ENTRADA_REJEITADA)) {
                    // Rejeicao de Cancelamento
                    processDenied(dischargeDetail, baseBankBillet, processingDate);

                } else {
                    // Comando nao Suportado
                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.DENIED, processingDate);
                    logManager.logWARN(
                        "Rejeitando retorno com comando nao suportado [nossoNumero=%s, codigoDeOcorrencia=%s, codigoDoErro=%s]",
                        dischargeDetail.getNossoNumero(), dischargeDetail.getCodigoDeOcorrencia(),
                        dischargeDetail.getCodigoDoPrimeiroErro());
                    continue;
                }
            }
        }
    }

    private BasePaymentBankBillet findBankBillet(BaseCompanyBankAccount baseCompanyBankAccount,
        SantanderCNAB400DischargeDetail dischargeDetail) {

        BasePaymentBankBillet bankBillet = null;

        // Procura nos boletos com base no seuNumero
        if ((dischargeDetail.getSeuNumero() != null) && (dischargeDetail.getSeuNumero() != 0))
            bankBillet = getBankBilletByYourNumber(baseCompanyBankAccount, dischargeDetail.getSeuNumero());

        else
            // Procura nos boletos no nossoNumero
            bankBillet = getBankBilletByOurNumber(baseCompanyBankAccount, dischargeDetail.getNossoNumero());
        if (bankBillet != null)
            return bankBillet;

        // Procura nas requisicoes de registro com base no seuNumero
        if ((dischargeDetail.getSeuNumero() != null) && (dischargeDetail.getSeuNumero() != 0)) {

            List<SantanderBilletTicketRequest> registryRequests = billetTicketRequestRepository
                .findByBankBilletCompanyBankAccountAndTituloSeuNumero(baseCompanyBankAccount,
                    dischargeDetail.getSeuNumero());

            if (CollectionUtils.isNotEmpty(registryRequests)) {
                SantanderBilletTicketRequest registryRequest = registryRequests.get(0);
                bankBillet = registryRequest.getBankBillet();
            }
        }

        return bankBillet;
    }

    private void processRegistration(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de registro");
        cnabExchangeService.processRegisteredDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de registro processado com sucesso");
    }

    private void processPayment(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {
        logManager.logDEBUG("Processando retorno bancario de pagamento");

        int paidAmount = dischargeDetail.getValorTotalRecebido();
        int fee = dischargeDetail.getValorDaTarifaCobrada();
        int releasedAmount = paidAmount - fee;
        cnabExchangeService.processPaidDischarge(dischargeDetail, baseBankBillet, processingDate,
            dischargeDetail.getDataDaOcorrencia(), paidAmount, fee, dischargeDetail.getDataDoCredito(), releasedAmount);

        logManager.logDEBUG("Retorno bancario de pagamento processado com sucesso");
    }

    private void processCancellation(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de cancelamento");
        cnabExchangeService.processCancelledDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de cancelamento processado com sucesso");
    }

    private void processPurge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de baixa");
        cnabExchangeService.processPurgeDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de baixa processado com sucesso");
    }

    private void processDenied(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario com erro");

        // Mapeia o codigo de ocorrencia de erro
        BaseResultCode resultCode = resultCodeTranslator
            .translateOcorrenciaDeErroToResultCode(dischargeDetail.getCodigoDoPrimeiroErro(), dischargeDetail.getNossoNumero());

        if (resultCode != null) {
            cnabExchangeService.processDeniedDischarge(dischargeDetail, baseBankBillet, processingDate, resultCode);
            logManager.logDEBUG("Retorno bancario de rejeicao processado com sucesso");

        } else {
            cnabExchangeService.processDeniedDischarge(dischargeDetail, baseBankBillet, processingDate, null);
            logManager.logWARN(
                "Ignorando retorno de rejeicao com ocorrencia e erro nao suportados [codigoDeOcorrencia=%s, codigoDoErro=%s]",
                dischargeDetail.getCodigoDeOcorrencia(), dischargeDetail.getCodigoDoPrimeiroErro());
        }
    }

}
