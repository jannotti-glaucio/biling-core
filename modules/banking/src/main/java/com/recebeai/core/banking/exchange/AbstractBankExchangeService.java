package tech.jannotti.billing.core.banking.exchange;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.exception.discharge.AlreadyPaidBilletBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.InvalidBilletPurgeBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.InvalidPaymentStatusBankingException;
import tech.jannotti.billing.core.banking.exception.discharge.UnknowDischargeSourceBankingException;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.enums.BankDischargeOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankDischarge;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankDischargeRepository;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRemittanceRepository;
import tech.jannotti.billing.core.services.ResultCodeService;
import tech.jannotti.billing.core.services.bank.BankingService;
import tech.jannotti.billing.core.services.payment.BankBilletService;
import tech.jannotti.billing.core.services.payment.PaymentService;

public class AbstractBankExchangeService {

    private static final LogManager logManager = LogFactory.getManager(AbstractBankExchangeService.class);

    @Autowired
    private BankRemittanceRepository bankRemittanceRepository;

    @Autowired
    private BankDischargeRepository bankDischargeRepository;

    @Autowired
    protected BankingService bankingService;

    @Autowired
    private ResultCodeService resultCodeService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BankBilletService bankBilletService;

    private BaseBankRemittance getProcessedBankRemittance(BasePaymentBankBillet bankBillet,
        BankRemittanceOperationEnum operation) {

        BaseBankRemittance bankRemittance = bankRemittanceRepository
            .getFirstByPaymentAndOperationAndStatusOrderByProcessingDateDesc(bankBillet, operation,
                BankRemittanceStatusEnum.PROCESSED);
        if (bankRemittance == null) {
            logManager.logWARN("Retorno bancario sem registro de remessa [paymentToken=%s]", bankBillet.getToken());
            return null;

        } else {
            return bankRemittance;
        }
    }

    @Transactional
    protected void processBaseRegisteredDischarge(BasePaymentBankBillet bankBillet, LocalDateTime processingDate,
        long ourNumber, long yourNumber, int registrationCost) throws InvalidPaymentStatusBankingException {

        // Se o registro tinha sido concluido com sucesso ou esteja com um cancelamento solicitado
        if (bankBillet.getStatus().equals(PaymentStatusEnum.AUTHORIZED)
            || bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED)) {

            // Confirma o registro com sucesso
            bankBilletService.confirmRegistration(bankBillet, registrationCost);
            return;

        } else if (bankBillet.getStatus().equals(PaymentStatusEnum.ERROR)) {
            // Se o registro tinha dado erro, pede o cancelamento
            logManager.logWARN("Solicitando o cancelamento de boleto registrado apesar do erro no registro [paymentToken=%s]",
                bankBillet.getToken());
            bankBilletService.cancelUnregistered(bankBillet, ourNumber, yourNumber);

        } else
            throw new InvalidPaymentStatusBankingException(bankBillet, BankDischargeOperationEnum.REGISTRY);
    }

    @Transactional
    protected BaseBankDischarge processBaseDeniedDischarge(BasePaymentBankBillet bankBillet, LocalDateTime processingDate,
        BaseResultCode resultCode) throws UnknowDischargeSourceBankingException, AlreadyPaidBilletBankingException {

        BaseBankRemittance bankRemittance = null;

        if (bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED)) {
            // Procura a remessa de cancelamento desse erro
            bankRemittance = getProcessedBankRemittance(bankBillet, BankRemittanceOperationEnum.CANCELLATION);

        } else if (bankBillet.getStatus().equals(PaymentStatusEnum.CAPTURED)) {
            // Boleto jah foi pago
            throw new AlreadyPaidBilletBankingException();

        } else
            throw new UnknowDischargeSourceBankingException(bankBillet);

        BankDischargeOperationEnum operation = null;
        if (bankRemittance == null)
            operation = BankDischargeOperationEnum.UNKNOW;
        else if (bankRemittance.getOperation().equals(BankRemittanceOperationEnum.CANCELLATION))
            operation = BankDischargeOperationEnum.CANCELLATION;
        else
            operation = BankDischargeOperationEnum.UNKNOW;

        // Registra o retorno bancario de registro com erro
        BaseBankDischarge bankDischarge = new BaseBankDischarge(bankBillet, bankBillet.getCompanyBankAccount(), bankRemittance,
            operation, processingDate, resultCode);
        bankDischargeRepository.save(bankDischarge);

        // Se o erro foi de um cancelametno volta o pagamento pro status autorizado
        if (bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED)) {
            logManager.logWARN(
                "Mudando o boleto pra negado pois o processamento do cancelamento foi negado [paymentToken=%s]",
                bankBillet.getToken());
            paymentService.deny(bankBillet);

        } else
            throw new UnknowDischargeSourceBankingException(bankBillet);

        return bankDischarge;
    }

    @Transactional
    protected BaseBankDischarge processBasePaidDischarge(BasePaymentBankBillet bankBillet, LocalDateTime processingDate,
        LocalDate paymentDate, int paidAmount, int paymentCost, LocalDate releaseDate, int releasedAmount)
        throws InvalidPaymentStatusBankingException {

        // Valida o status do pagamento
        if (!bankBillet.getStatus().equals(PaymentStatusEnum.AUTHORIZED)
            && !bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED))
            throw new InvalidPaymentStatusBankingException(bankBillet, BankDischargeOperationEnum.PAYMENT);

        if (bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED))
            logManager.logINFO("Processando pagamento de boleto com cancelamento solicitado [paymentToken=%s]",
                bankBillet.getToken());

        // Registra o retorno bancario de pagamento
        BaseResultCode resultCode = resultCodeService.getSuccessResultCode();
        BaseBankDischarge bankDischarge = new BaseBankDischarge(bankBillet, bankBillet.getCompanyBankAccount(),
            BankDischargeOperationEnum.PAYMENT, processingDate, resultCode);
        bankDischargeRepository.save(bankDischarge);

        // Atualiza o status do pagamento para capturado
        paymentService.capture(bankBillet, paymentDate, paidAmount, paymentCost, releaseDate, releasedAmount);

        return bankDischarge;
    }

    @Transactional
    protected BaseBankDischarge processBaseCancelledDischarge(BasePaymentBankBillet bankBillet, LocalDateTime processingDate)
        throws InvalidPaymentStatusBankingException {

        // Valida o status do pagamento
        if (!bankBillet.getStatus().equals(PaymentStatusEnum.CANCELATION_REQUESTED))
            throw new InvalidPaymentStatusBankingException(bankBillet, BankDischargeOperationEnum.CANCELLATION);

        // Procura a remessa desse cancelamento
        BaseBankRemittance bankRemittance = getProcessedBankRemittance(bankBillet, BankRemittanceOperationEnum.CANCELLATION);

        // Registra o retorno bancario de cancelamento no core
        BaseResultCode resultCode = resultCodeService.getSuccessResultCode();
        BaseBankDischarge bankDischarge = new BaseBankDischarge(bankBillet, bankBillet.getCompanyBankAccount(), bankRemittance,
            BankDischargeOperationEnum.CANCELLATION, processingDate, resultCode);
        bankDischargeRepository.save(bankDischarge);

        // Atualiza o status do pagamento para cancelado
        paymentService.cancel(bankBillet, processingDate);

        return bankDischarge;
    }

    @Transactional
    protected BaseBankDischarge processBasePurgeDischarge(BasePaymentBankBillet bankBillet, LocalDateTime processingDate)
        throws InvalidPaymentStatusBankingException, InvalidBilletPurgeBankingException {

        // Valida se o boleto eh pagavel apos o vencimento
        if (bankBillet.isExpiredPayment())
            throw new InvalidBilletPurgeBankingException(bankBillet);

        // Valida o status do pagamento
        if (!bankBillet.getStatus().equals(PaymentStatusEnum.AUTHORIZED))
            throw new InvalidPaymentStatusBankingException(bankBillet, BankDischargeOperationEnum.PURGE);

        // Registra o retorno bancario de baixa no core
        BaseResultCode resultCode = resultCodeService.getSuccessResultCode();
        BaseBankDischarge bankDischarge = new BaseBankDischarge(bankBillet, bankBillet.getCompanyBankAccount(),
            BankDischargeOperationEnum.PURGE, processingDate, resultCode);
        bankDischargeRepository.save(bankDischarge);

        // Atualiza o status do pagamento e e fatura para cancelado
        paymentService.purge(bankBillet, processingDate);

        return bankDischarge;
    }

}
