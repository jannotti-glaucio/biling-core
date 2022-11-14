package tech.jannotti.billing.core.banking.santander.exchange.cnab400;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.jannotti.billing.core.banking.exception.discharge.AlreadyPaidBilletBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.InvalidBilletPurgeBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.InvalidPaymentStatusBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.UnknowDischargeSourceBankingException;
import tech.jannotti.billing.core.banking.exchange.AbstractBankExchangeService;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400DischargeFile;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceDetail;
import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderCNAB400RemittanceFile;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400DischargeDetailRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400DischargeFileRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400RemittanceDetailRepository;
import tech.jannotti.billing.core.banking.santander.persistence.repository.CNAB400RemittanceFileRepository;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankDischarge;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@Service("banking.santander.cnab400ExchangeService")
class CNAB400ExchangeService extends AbstractBankExchangeService {

    private static final LogManager logManager = LogFactory.getManager(CNAB400ExchangeService.class);

    @Autowired
    private CNAB400RemittanceFileRepository remittanceFileRepository;

    @Autowired
    private CNAB400RemittanceDetailRepository remittanceDetailRepository;

    @Autowired
    private CNAB400DischargeFileRepository dischargeFileRepository;

    @Autowired
    private CNAB400DischargeDetailRepository dischargeDetailRepository;

    @Transactional
    public void saveRemittanceFileAndUpdateRemittancesStatus(SantanderCNAB400RemittanceFile remittanceFile,
        BankRemittanceStatusEnum status) {

        remittanceFileRepository.save(remittanceFile);

        for (SantanderCNAB400RemittanceDetail remittanceDetail : remittanceFile.getDetails()) {
            remittanceDetail.setRemittanceFile(remittanceFile);
            remittanceDetailRepository.save(remittanceDetail);

            bankingService.updateRemittanceStatus(remittanceDetail.getBaseBankRemittance(), status);
        }
    }

    @Transactional
    public void saveDischargeFile(SantanderCNAB400DischargeFile dischargeFile) {

        dischargeFileRepository.save(dischargeFile);

        if (dischargeFile.getDetails() != null) {
            for (SantanderCNAB400DischargeDetail dischargeDetail : dischargeFile.getDetails()) {
                dischargeDetail.setDischargeFile(dischargeFile);
                dischargeDetail.setStatus(BankDischargeStatusEnum.PENDING);
                dischargeDetailRepository.save(dischargeDetail);
            }
        }
    }

    @Transactional
    public void updateDischargeStatus(SantanderCNAB400DischargeDetail dischargeDetail, BankDischargeStatusEnum status,
        LocalDateTime processingDate) {
        dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), status, processingDate);
    }

    @Transactional
    public void processRegisteredDischarge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        try {
            processBaseRegisteredDischarge(baseBankBillet, processingDate, dischargeDetail.getNossoNumero(),
                dischargeDetail.getSeuNumero(), dischargeDetail.getValorDaTarifaCobrada());

        } catch (InvalidPaymentStatusBankingException e) {
            logManager.logERROR("Rejeitando registro pois o boleto nao estah no status esperado [nossoNumero=%s]", e,
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(),
                BankDischargeStatusEnum.DENIED, processingDate);
            return;
        }

        // Atualiza o status do retorno bancario processado
        dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.PROCESSED,
            processingDate);
    }

    @Transactional
    public void processDeniedDischarge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate, BaseResultCode resultCode) {

        BaseBankDischarge baseBankDischarge = null;
        try {
            baseBankDischarge = processBaseDeniedDischarge(baseBankBillet, processingDate, resultCode);
        } catch (UnknowDischargeSourceBankingException e) {
            logManager.logERROR("Rejeitando retorno de rejeicao pois nao foi localizada a origem desse retorno [nossoNumero=%s]",
                e, dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.DENIED,
                processingDate);
            return;

        } catch (AlreadyPaidBilletBankingException e) {
            logManager.logINFO("Ignorando retorno de rejeicao pois o boleto jah estah pago [nossoNumero=%s]",
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.IGNORED,
                processingDate);
            return;
        }

        // Atualiza o status do retorno bancario com erro
        dischargeDetailRepository.updateBaseBankDischargeAndStatusAndProcessingDateById(dischargeDetail.getId(),
            baseBankDischarge, BankDischargeStatusEnum.DENIED, processingDate);
    }

    @Transactional
    public void processPaidDischarge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate, LocalDate paymentDate, int paidAmount, int fee, LocalDate releaseDate, int releasedAmount) {

        BaseBankDischarge baseBankDischarge = null;
        try {
            baseBankDischarge = processBasePaidDischarge(baseBankBillet, processingDate, paymentDate, paidAmount, fee,
                releaseDate, releasedAmount);

        } catch (InvalidPaymentStatusBankingException e) {
            logManager.logERROR("Rejeitando pagamento pois o boleto nao estah no status esperado [nossoNumero=%s]", e,
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.DENIED,
                processingDate);
            return;
        }

        // Atualiza o status do retorno bancario processado
        dischargeDetailRepository.updateBaseBankDischargeAndStatusAndProcessingDateById(dischargeDetail.getId(),
            baseBankDischarge, BankDischargeStatusEnum.PROCESSED, processingDate);
    }

    @Transactional
    public void processCancelledDischarge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        BaseBankDischarge baseBankDischarge = null;
        try {
            baseBankDischarge = processBaseCancelledDischarge(baseBankBillet, processingDate);

        } catch (InvalidPaymentStatusBankingException e) {
            logManager.logERROR("Rejeitando cancelamento pois o boleto nao estah no status esperado [nossoNumero=%s]", e,
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.DENIED,
                processingDate);
            return;
        }

        // Atualiza o status do retorno bancario processado
        dischargeDetailRepository.updateBaseBankDischargeAndStatusAndProcessingDateById(dischargeDetail.getId(),
            baseBankDischarge, BankDischargeStatusEnum.PROCESSED, processingDate);
    }

    @Transactional
    public void processPurgeDischarge(SantanderCNAB400DischargeDetail dischargeDetail, BasePaymentBankBillet baseBankBillet,
        LocalDateTime processingDate) {

        BaseBankDischarge baseBankDischarge = null;
        try {
            baseBankDischarge = processBasePurgeDischarge(baseBankBillet, processingDate);

        } catch (InvalidPaymentStatusBankingException e) {
            logManager.logERROR("Rejeitando baixa pois o boleto nao estah no status esperado [nossoNumero=%s]", e,
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.DENIED,
                processingDate);
            return;

        } catch (InvalidBilletPurgeBankingException e) {
            logManager.logERROR("Rejeitando baixa pois o boleto foi registrado para baixa automatica [nossoNumero=%s]", e,
                dischargeDetail.getNossoNumero());
            dischargeDetailRepository.updateStatusAndProcessingDateById(dischargeDetail.getId(), BankDischargeStatusEnum.DENIED,
                processingDate);
            return;
        }

        // Atualiza o status do retorno bancario processado
        dischargeDetailRepository.updateBaseBankDischargeAndStatusAndProcessingDateById(dischargeDetail.getId(),
            baseBankDischarge, BankDischargeStatusEnum.PROCESSED, processingDate);
    }

}
