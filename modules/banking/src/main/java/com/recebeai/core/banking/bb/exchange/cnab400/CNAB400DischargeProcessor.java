package tech.jannotti.billing.core.banking.bb.exchange.cnab400;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.banking.bb.BancoBrasilConstants;
import tech.jannotti.billing.core.banking.bb.exchange.ResultCodeTranslator;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryRequest;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilCompanyBankAccount;
import tech.jannotti.billing.core.banking.bb.persistence.repository.BilletRegistryRequestRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CNAB400DischargeDetailRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CNAB400DischargeFileRepository;
import tech.jannotti.billing.core.banking.bb.persistence.repository.CompanyBankAccountRepository;
import tech.jannotti.billing.core.banking.exchange.AbstractBankDischargeProcessor;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Component("banking.bb.cnab400DischargeProcessor")
public class CNAB400DischargeProcessor extends AbstractBankDischargeProcessor {

    private static final LogManager logManager = LogFactory.getManager(CNAB400DischargeProcessor.class);

    private static final Integer COMANDO_ENTRADA_CONFIRMADA = 2;
    private static final Integer NATUREZA_MEIO_MAGNETICO = 0;

    private static final Integer COMANDO_LIQUIDACAO_NORMAL = 6;
    private static final Integer NATUREZA_LIQUIDACAO_NORMAL = 1;

    private static final Integer COMANDO_BAIXA_DE_TITULO = 9;
    private static final Integer NATUREZA_BAIXA_AUTOMATICA = 90;

    private static final Integer COMANDO_BAIXA_SOLICITADA = 10;
    private static final Integer NATUREZA_SOLICITADA_CLIENTE = 0;

    private static final Integer COMANDO_ENTRADA_REJEITADA = 3;
    private static final Integer COMANDO_MANUTENCAO_TITULO_VENCIDO = 28;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private CNAB400DischargeFileRepository dischargeFileRepository;

    @Autowired
    private CNAB400DischargeDetailRepository dischargeDetailRepository;

    @Autowired
    private BilletRegistryRequestRepository billetRegistryRequestRepository;

    @Autowired
    private CNAB400ExchangeService cnabExchangeService;

    @Autowired
    private ResultCodeTranslator resultCodeTranslator;

    @Override
    protected String getBankNumber() {
        return BancoBrasilConstants.BANK_NUMBER;
    }

    @Override
    public void processDischarges(BaseCompanyBankAccount baseCompanyBankAccount) {

        BancoBrasilCompanyBankAccount companyBankAccount = companyBankAccountRepository
            .getByBaseCompanyBankAccount(baseCompanyBankAccount);

        List<BancoBrasilCNAB400DischargeFile> dischargeFiles = dischargeFileRepository.findByCompanyBankAccountAndDischargeStatus(
            companyBankAccount, BankDischargeStatusEnum.PENDING);

        if (CollectionUtils.isEmpty(dischargeFiles)) {
            logManager.logDEBUG("Nao ha arquivos para processar");
            return;
        }

        for (BancoBrasilCNAB400DischargeFile dischargeFile : dischargeFiles) {
            logManager.logINFO("Processando Arquivo de Retorno [id=%s]", dischargeFile.getId());

            List<BancoBrasilCNAB400DischargeDetail> dischargeDetails = dischargeDetailRepository
                .findByDischargeFileAndStatus(dischargeFile, BankDischargeStatusEnum.PENDING);

            if (dischargeDetails == null) {
                logManager.logDEBUG("Arquivo nao tem retornos bancarios para processar");
                continue;
            }

            for (BancoBrasilCNAB400DischargeDetail dischargeDetail : dischargeDetails) {
                logManager.logINFO("Processando Retorno Bancario [id=%s]", dischargeDetail.getId());

                LocalDateTime processingDate = DateTimeHelper.getNowDateTime();

                BasePaymentBankBillet baseBankBillet = findBankBillet(baseCompanyBankAccount, dischargeDetail);
                if (baseBankBillet == null) {

                    // Rejeitando registro de boleto nao localizado
                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.DENIED, processingDate);
                    logManager.logWARN(
                        "Rejeitando retorno pois o boleto nao foi localizado [nossoNumero=%s, comando=%s, naturezaRecebimento=%s]",
                        dischargeDetail.getNossoNumero(), dischargeDetail.getComando(), dischargeDetail.getNaturezaRecebimento());
                    continue;
                }

                // Validando se o status do boleto estah divergente do comando recebido
                if (!baseBankBillet.getStatus().equals(PaymentStatusEnum.AUTHORIZED)
                    && dischargeDetail.getComando().equals(COMANDO_MANUTENCAO_TITULO_VENCIDO)) {

                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.DENIED, processingDate);
                    logManager.logWARN("Rejeitando retorno com comando divergente do aguardado pelo status atual do boleto "
                        + "[status=%s, nossoNumero=%s, comando=%s, naturezaRecebimento=%s]", baseBankBillet.getStatus(),
                        dischargeDetail.getNossoNumero(), dischargeDetail.getComando(), dischargeDetail.getNaturezaRecebimento());
                    continue;
                }

                // Ignorando comandos que nao modificam status do boleto pendente
                if (dischargeDetail.getComando().equals(COMANDO_MANUTENCAO_TITULO_VENCIDO)) {

                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.IGNORED, processingDate);
                    logManager.logDEBUG(
                        "Ignorando retorno com comando que nao modifica o boleto [nossoNumero=%s, comando=%s, naturezaRecebimento=%s]",
                        dischargeDetail.getNossoNumero(), dischargeDetail.getComando(), dischargeDetail.getNaturezaRecebimento());
                    continue;
                }

                if (dischargeDetail.getComando().equals(COMANDO_ENTRADA_CONFIRMADA)
                    && dischargeDetail.getNaturezaRecebimento().equals(NATUREZA_MEIO_MAGNETICO)) {
                    // Registro
                    processRegistration(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getComando().equals(COMANDO_LIQUIDACAO_NORMAL)
                    && dischargeDetail.getNaturezaRecebimento().equals(NATUREZA_LIQUIDACAO_NORMAL)) {
                    // Pagamento
                    processPayment(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getComando().equals(COMANDO_BAIXA_SOLICITADA)
                    && dischargeDetail.getNaturezaRecebimento().equals(NATUREZA_SOLICITADA_CLIENTE)) {
                    // Cancelamento
                    processCancellation(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getComando().equals(COMANDO_BAIXA_DE_TITULO)
                    && dischargeDetail.getNaturezaRecebimento().equals(NATUREZA_BAIXA_AUTOMATICA)) {
                    // Baixa Automatica
                    processPurge(dischargeDetail, baseBankBillet, processingDate);

                } else if (dischargeDetail.getComando().equals(COMANDO_ENTRADA_REJEITADA)) {
                    // Rejeicao de Cancelamento
                    processDenied(dischargeDetail, baseBankBillet, processingDate);

                } else {
                    // Comando nao Suportado
                    cnabExchangeService.updateDischargeStatus(dischargeDetail, BankDischargeStatusEnum.DENIED, processingDate);
                    logManager.logWARN(
                        "Rejeitando retorno com comando nao suportado [nossoNumero=%s, comando=%s, naturezaRecebimento=%s]",
                        dischargeDetail.getNossoNumero(), dischargeDetail.getComando(), dischargeDetail.getNaturezaRecebimento());
                    continue;
                }
            }
        }
    }

    private BasePaymentBankBillet findBankBillet(BaseCompanyBankAccount baseCompanyBankAccount,
        BancoBrasilCNAB400DischargeDetail dischargeDetail) {

        if (dischargeDetail.getNumeroTituloCedente() == null)
            return null;

        BasePaymentBankBillet bankBillet = null;

        // Procura nos boletos com base no numeroTituloCedente/seuNumero
        bankBillet = getBankBilletByYourNumber(baseCompanyBankAccount, dischargeDetail.getNumeroTituloCedente());
        if (bankBillet != null)
            return bankBillet;

        // Procura nas requisicoes de registro com base no numeroTituloCedente
        List<BancoBrasilBilletRegistryRequest> registryRequests = billetRegistryRequestRepository
            .findByBankBilletCompanyBankAccountAndTextoNumeroTituloBeneficiario(baseCompanyBankAccount,
                dischargeDetail.getNumeroTituloCedente());

        if (CollectionUtils.isNotEmpty(registryRequests)) {
            BancoBrasilBilletRegistryRequest registryRequest = registryRequests.get(0);
            bankBillet = registryRequest.getBankBillet();
        }

        return bankBillet;
    }

    private void processRegistration(BancoBrasilCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de registro");
        cnabExchangeService.processRegisteredDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de registro processado com sucesso");
    }

    private void processPayment(BancoBrasilCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {
        logManager.logDEBUG("Processando retorno bancario de pagamento");

        int paidAmount = dischargeDetail.getValorRecebido();
        int fee = dischargeDetail.getValorTarifa();
        int releasedAmount = dischargeDetail.getValorLancamento();
        cnabExchangeService.processPaidDischarge(dischargeDetail, baseBankBillet, processingDate,
            dischargeDetail.getDataLiquidacao(), paidAmount, fee, dischargeDetail.getDataCredito(), releasedAmount);

        logManager.logDEBUG("Retorno bancario de pagamento processado com sucesso");
    }

    private void processCancellation(BancoBrasilCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de cancelamento");
        cnabExchangeService.processCancelledDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de cancelamento processado com sucesso");
    }

    private void processPurge(BancoBrasilCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario de baixa");
        cnabExchangeService.processPurgeDischarge(dischargeDetail, baseBankBillet, processingDate);
        logManager.logDEBUG("Retorno bancario de baixa processado com sucesso");
    }

    private void processDenied(BancoBrasilCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        logManager.logDEBUG("Processando retorno bancario com erro");

        // Mapeia o codigo de ocorrencia
        BaseResultCode resultCode = resultCodeTranslator.translateCNAB400NaturezaRecebimentoToResultCode(
            dischargeDetail.getNaturezaRecebimento(), dischargeDetail.getNumeroTituloCedente());

        if (resultCode != null) {
            cnabExchangeService.processDeniedDischarge(dischargeDetail, baseBankBillet, processingDate, resultCode);
            logManager.logDEBUG("Retorno bancario de rejeicao processado com sucesso");

        } else {
            logManager.logWARN("Ignorando retorno de rejeicao com natureza de recebimento nao suportada [naturezaRecebimento=%s]",
                dischargeDetail.getNaturezaRecebimento());
        }
    }

}
