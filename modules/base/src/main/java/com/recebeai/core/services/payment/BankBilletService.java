package tech.jannotti.billing.core.services.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.connector.banking.BankingConnector;
import tech.jannotti.billing.core.connector.banking.BankingConnectorManager;
import tech.jannotti.billing.core.connector.banking.response.PrintBankBilletConnectorResponse;
import tech.jannotti.billing.core.connector.banking.response.RegisterBankBilletConnectorResponse;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceDeliveryModeEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceSourceEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerAddress;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRemittanceRepository;
import tech.jannotti.billing.core.persistence.repository.base.payment.PaymentBankBilletRepository;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class BankBilletService extends AbstractPaymentService {

    private LogManager logManager = LogFactory.getManager(BankBilletService.class);

    @Autowired
    private PaymentBankBilletRepository bankBilletRepository;

    @Autowired
    private BankRemittanceRepository bankRemittanceRepository;

    @Autowired
    private BankingConnectorManager bankingConnectorManager;

    @Autowired
    private PaymentService paymentService;

    public BasePaymentBankBillet getAuthorized(BaseInvoice invoice) {
        PaymentStatusEnum[] status = new PaymentStatusEnum[] { PaymentStatusEnum.AUTHORIZED };
        return bankBilletRepository.getByInvoiceAndStatusIn(invoice, status);
    }

    @Transactional(dontRollbackOn = ResultCodeServiceException.class)
    public BasePaymentBankBillet add(BaseInvoice invoice, LocalDate penaltyStartDate) {

        BaseCompanyBankAccount bankAccount = invoice.getCustomer().getDealer().getCompanyBankAccount();
        if (bankAccount == null)
            throw new ResultCodeServiceException(CODE_DEALER_BANK_ACCOUNT_MISSING);

        validateExpirationDate(invoice, bankAccount);
        validatePenaltyStartDate(invoice, penaltyStartDate);

        BasePaymentBankBillet bankBillet = new BasePaymentBankBillet();
        bankBillet.setInvoice(invoice);
        bankBillet.setToken(generateToken());
        bankBillet.setAmount(invoice.getAmount());
        bankBillet.setPaymentMethod(invoice.getPaymentMethod());
        bankBillet.setStatus(PaymentStatusEnum.PENDING);
        bankBillet.setCreationDate(DateTimeHelper.getNowDateTime());
        bankBillet.setCompanyBankAccount(bankAccount);
        bankBillet.setExpirationDate(invoice.getExpirationDate());
        bankBillet.setIssueDate(DateTimeHelper.getNowDate());

        BaseCustomer customer = invoice.getCustomer();
        bankBillet.setPayerName(customer.getName());
        bankBillet.setPayerDocumentType(customer.getDocumentType());
        bankBillet.setPayerDocumentNumber(customer.getDocumentNumber());

        BaseCustomerAddress customerAddress = customerService.getBillingAddress(invoice.getCustomer());
        bankBillet.setPayerAddressStreet(customerAddress.getStreet());
        bankBillet.setPayerAddressNumber(customerAddress.getNumber());
        bankBillet.setPayerAddressComplement(customerAddress.getComplement());
        bankBillet.setPayerAddressDistrict(customerAddress.getDistrict());
        bankBillet.setPayerAddressZipCode(customerAddress.getZipCode());
        bankBillet.setPayerAddressCity(customerAddress.getCity());
        bankBillet.setPayerAddressState(customerAddress.getState());

        BaseDealer dealer = customer.getDealer();
        bankBillet.setExpiredPayment(dealer.isBankBilletExpiredPayment());
        if (bankBillet.isExpiredPayment()) {
            bankBillet.setPenaltyPercent(dealer.getBankBilletPenaltyPercent());
            bankBillet.setInterestPercent(dealer.getBankBilletInterestPercent());
            if (penaltyStartDate != null)
                bankBillet.setPenaltyStartDate(penaltyStartDate);
            else
                bankBillet.setPenaltyStartDate(invoice.getExpirationDate().plusDays(1));
        }

        bankBilletRepository.save(bankBillet);

        // Cria a remessa de registro
        BaseBankRemittance bankRemittance = new BaseBankRemittance();
        bankRemittance.setSource(BankRemittanceSourceEnum.BANK_BILLET);
        bankRemittance.setBankAccount(bankAccount);
        bankRemittance.setPayment(bankBillet);
        bankRemittance.setOperation(BankRemittanceOperationEnum.REGISTRY);
        bankRemittance.setDeliveryMode(BankRemittanceDeliveryModeEnum.WEB_SERVICES);
        bankRemittance.setStatus(BankRemittanceStatusEnum.PENDING);
        bankRemittance.setCreationDate(DateTimeHelper.getNowDateTime());
        bankRemittanceRepository.save(bankRemittance);

        // Define o numero do documento do boleto
        if (invoice.getDocumentNumber() != null)
            bankBillet.setDocumentNumber(invoice.getDocumentNumber());
        else
            bankBillet.setDocumentNumber(bankBillet.getId());

        BankingConnector connector = bankingConnectorManager.getConnector(bankAccount.getBank().getConnectorPath());

        RegisterBankBilletConnectorResponse connectorResponse = null;
        BaseResultCode resultCode = null;
        String exceptionMessage = null;
        try {
            // Solicita o registro atraves do connector
            connectorResponse = connector.registerBankBillet(bankRemittance);
            resultCode = connectorResponse.getResultCode();

        } catch (ResultCodeServiceException e) {
            resultCode = getResultCode(e.getResultCodeKey());

            exceptionMessage = ExceptionUtils.getRootCauseMessage(e);
            logManager.logERROR("Erro registrando boleto", e);

        } catch (Exception e) {
            resultCode = getGenericErrorResultCode();

            exceptionMessage = ExceptionUtils.getRootCauseMessage(e);
            logManager.logERROR("Erro registrando boleto", e);
        }

        if (resultCode.isSuccess()) {
            // Atualiza a remessa com o resultado do processamento
            bankRemittance.setProcessingDate(DateTimeHelper.getNowDateTime());
            bankRemittance.setStatus(BankRemittanceStatusEnum.PROCESSED);
            bankRemittanceRepository.save(bankRemittance);

            // Atualiza o boleto com os campos retornados no registro
            bankBillet.setStatus(PaymentStatusEnum.AUTHORIZED);
            bankBillet.setOurNumber(connectorResponse.getOurNumber());
            bankBillet.setYourNumber(connectorResponse.getYourNumber());
            bankBillet.setLineCode(connectorResponse.getLineCode());
            bankBilletRepository.save(bankBillet);

            return bankBillet;

        } else {
            // Atualiza a remessa com o erro no processamento
            bankRemittance.setProcessingDate(DateTimeHelper.getNowDateTime());
            bankRemittance.setStatus(BankRemittanceStatusEnum.ERROR);
            bankRemittance.setResultCode(resultCode);
            bankRemittance.setExceptionMessage(exceptionMessage);
            bankRemittanceRepository.save(bankRemittance);

            // Atualiza o boleto para o status de erro
            bankBilletRepository.updateStatusById(bankBillet.getId(), PaymentStatusEnum.ERROR);

            throw new ResultCodeServiceException(resultCode.getKey());
        }
    }

    private void validateExpirationDate(BaseInvoice invoice, BaseCompanyBankAccount bankAccount) {
        LocalDate currentDate = DateTimeHelper.getNowDate();

        if (!invoice.getExpirationDate().equals(currentDate) &&
            !invoice.getExpirationDate().isAfter(currentDate)) {

            String formatedCurrentDate = DateTimeHelper.formatToIsoDate(currentDate);
            throw new ResultCodeServiceException(CODE_INVOICE_INVALID_EXPIRATION_DATE, formatedCurrentDate);
        }
    }

    private void validatePenaltyStartDate(BaseInvoice invoice, LocalDate penaltyStartDate) {

        if ((penaltyStartDate != null) && !penaltyStartDate.isAfter(invoice.getExpirationDate()))
            throw new ResultCodeServiceException(CODE_PENALTY_START_DATE_GREATER_THAN_EXPIRATION_DATE);
    }

    @Transactional
    public void update(BaseInvoice invoice, LocalDate penaltyStartDate) {

        BasePaymentBankBillet bankBillet = getAuthorized(invoice);

        // Registra um novo boleto pra fatura
        add(invoice, penaltyStartDate);

        if (bankBillet == null)
            logManager.logDEBUG("Fatura [%s] nao tem boleto para ser cancelado", invoice.getToken());
        else {
            // Cancela o boleto antigo da fatura
            cancel(bankBillet);
        }
    }

    public byte[] print(BaseInvoice invoice) {

        BasePaymentBankBillet bankBillet = getAuthorized(invoice);
        if (bankBillet == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVOICE_WITHOUT_ACTIVE_BANK_BILLET);

        BaseCompanyBankAccount bankAccount = bankBillet.getCompanyBankAccount();
        BankingConnector connector = bankingConnectorManager.getConnector(bankAccount.getBank().getConnectorPath());

        PrintBankBilletConnectorResponse connectorResponse = connector.printBankBillet(bankBillet);

        if (!connectorResponse.isSuccess())
            throw new ResultCodeServiceException(connectorResponse.getResultCode().getKey());

        return connectorResponse.getBilletContent();
    }

    @Transactional
    public void confirmRegistration(BasePaymentBankBillet bankBillet, int registrationCost) {

        bankBillet.setRegistrationCost(registrationCost);

        bankBilletRepository.updateRegistrationCostById(bankBillet.getId(), registrationCost);
    }

    @Transactional
    public void cancel(BaseInvoice invoice) {

        BasePaymentBankBillet bankBillet = getAuthorized(invoice);
        if (bankBillet == null) {
            logManager.logDEBUG("Fatura [%s] nao tem boleto para ser cancelado", invoice.getToken());
            return;
        }

        cancel(bankBillet);
    }

    @Transactional
    public void forceCancelation(String token) {

        BasePaymentBankBillet bankBillet = bankBilletRepository.findByToken(token);
        if (bankBillet == null)
            throw new ResultCodeServiceException(CODE_NOT_FOUND_TOKEN_PARAMETER, token);

        doCancel(bankBillet);
    }

    @Transactional
    public void cancel(BasePaymentBankBillet bankBillet) {

        // Verifica se ja existe uma remessa de cancelamento pendente de processamento
        boolean existsPendingCancellation = bankRemittanceRepository.existsByPaymentAndOperationAndStatus(bankBillet,
            BankRemittanceOperationEnum.CANCELLATION, BankRemittanceStatusEnum.PENDING);
        // Se ja existe, nao envia outra remessa
        if (existsPendingCancellation)
            return;

        // Verifica se existe ja uma remessa de cancelada processada, mas ainda sem retorno
        boolean existsProcessedCancellation = bankRemittanceRepository.existsByPaymentAndOperationAndStatus(bankBillet,
            BankRemittanceOperationEnum.CANCELLATION, BankRemittanceStatusEnum.PROCESSED);
        // Se ja existe, nao permite o envio do cancelamento
        if (existsProcessedCancellation)
            throw new ResultCodeServiceException(CODE_BANK_BILLET_WAITING_CANCELLATION_RESPONSE);

        doCancel(bankBillet);
    }

    private void doCancel(BasePaymentBankBillet bankBillet) {

        // Cria a remessa de cancelamento
        BaseBankRemittance bankRemittance = new BaseBankRemittance();
        bankRemittance.setSource(BankRemittanceSourceEnum.BANK_BILLET);
        bankRemittance.setBankAccount(bankBillet.getCompanyBankAccount());
        bankRemittance.setPayment(bankBillet);
        bankRemittance.setOperation(BankRemittanceOperationEnum.CANCELLATION);
        bankRemittance.setDeliveryMode(BankRemittanceDeliveryModeEnum.FILE);
        bankRemittance.setStatus(BankRemittanceStatusEnum.PENDING);
        bankRemittance.setCreationDate(DateTimeHelper.getNowDateTime());
        bankRemittanceRepository.save(bankRemittance);

        // Atualiza o status do pagamento para cancelado
        LocalDateTime cancelatioRequestnDate = DateTimeHelper.getNowDateTime();
        paymentService.requestCancelation(bankBillet, cancelatioRequestnDate);
    }

    @Transactional
    public void cancelUnregistered(BasePaymentBankBillet bankBillet, long ourNumber, long yourNumber) {

        // Atualiza o numero do boleto
        bankBillet.setOurNumber(ourNumber);
        bankBillet.setYourNumber(yourNumber);

        bankBilletRepository.updateOurNumberAndYourNumberById(bankBillet.getId(), bankBillet.getOurNumber(),
            bankBillet.getYourNumber());

        // Efetura o cancelamento
        cancel(bankBillet);
    }

}
