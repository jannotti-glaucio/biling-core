package tech.jannotti.billing.core.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.constants.enums.AlertTypeConstants;
import tech.jannotti.billing.core.constants.enums.EmailTemplateConstants;
import tech.jannotti.billing.core.messaging.jms.MessageSenderHelper;
import tech.jannotti.billing.core.messaging.jms.MessagingConstants;
import tech.jannotti.billing.core.persistence.enums.AlertStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseAlert;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseAlertType;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseInvoiceAlert;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseWithdrawAlert;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseCompanyUser;
import tech.jannotti.billing.core.persistence.repository.base.alert.AlertTypeRepository;
import tech.jannotti.billing.core.persistence.repository.base.alert.InvoiceAlertRepository;
import tech.jannotti.billing.core.persistence.repository.base.alert.MarketWithdrawAlertRepository;
import tech.jannotti.billing.core.persistence.repository.base.user.CompanyUserRepository;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;
import tech.jannotti.billing.core.services.exception.ServiceException;

@Service
public class AlertingService extends AbstractService {

    @Autowired
    private AlertTypeRepository alertTypeRepository;

    @Autowired
    private InvoiceAlertRepository invoiceAlertRepository;

    @Autowired
    private MarketWithdrawAlertRepository withdrawAlertRepository;

    @Autowired
    private MessageSenderHelper messageSenderHelper;

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EMailService emailService;

    public void dispatchInvoiceAlert(BaseInvoice invoice, MediaTypeEnum mediaType, BaseAlertType alertType) {

        if ((mediaType.equals(MediaTypeEnum.EMAIL)) && StringUtils.isBlank(invoice.getCustomer().getEmail()))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_CUSTOMER_EMAIL_REQUIRED_TO_SEND);

        BaseInvoiceAlert invoiceAlert = new BaseInvoiceAlert();
        buildAlert(invoiceAlert, mediaType, alertType);
        invoiceAlert.setInvoice(invoice);
        invoiceAlertRepository.save(invoiceAlert);

        // Envia pra fila de envio de alertas de faturas
        messageSenderHelper.send(MessagingConstants.INVOICE_ALERTS_QUEUE, invoiceAlert.getId());
    }

    public void dispatchMarketWithdrawAlert(BaseMarketWithdraw withDraw, MediaTypeEnum mediaType, BaseAlertType alertType) {

        BaseWithdrawAlert withdrawAlert = new BaseWithdrawAlert();
        buildAlert(withdrawAlert, mediaType, alertType);
        withdrawAlert.setWithdraw(withDraw);

        withdrawAlertRepository.save(withdrawAlert);

        // Envia pra fila de envio de alertas de faturas
        messageSenderHelper.send(MessagingConstants.MARKET_WITHDRAW_ALERTS_QUEUE, withdrawAlert.getId());
    }

    public void dispatchInvoiceAlert(BaseInvoice invoice, MediaTypeEnum mediaType, AlertTypeConstants alertType) {

        BaseAlertType alertTypeEntity = alertTypeRepository.getByCode(alertType.getCode());
        dispatchInvoiceAlert(invoice, mediaType, alertTypeEntity);
    }

    public void dispatchWithdrawAlert(BaseMarketWithdraw withdraw, MediaTypeEnum mediaType, AlertTypeConstants alertType) {

        BaseAlertType alertTypeEntity = alertTypeRepository.getByCode(alertType.getCode());
        dispatchMarketWithdrawAlert(withdraw, mediaType, alertTypeEntity);
    }

    private void buildAlert(BaseAlert alert, MediaTypeEnum mediaType, BaseAlertType alertType) {

        alert.setMediaType(mediaType);
        alert.setAlertType(alertType);
        alert.setStatus(AlertStatusEnum.PENDING);
        alert.setCreationDate(DateTimeHelper.getNowDateTime());
    }

    public void sendInvoiceAlert(long invoiceAlertId) {

        BaseInvoiceAlert invoiceAlert = invoiceAlertRepository.findOne(invoiceAlertId);

        BaseInvoice invoice = invoiceAlert.getInvoice();
        invoiceService.load(invoice);

        if (invoiceAlert.getMediaType().equals(MediaTypeEnum.EMAIL)) {

            // Descobre o template do email
            EmailTemplateConstants emailTemplate = mapInvoiceEmailTemplate(invoiceAlert.getAlertType(),
                invoiceAlert.getInvoice());

            if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {
                byte[] atachment = invoiceService.printBankBillet(invoice);
                emailService.sendInvoiceEmail(invoiceAlert, atachment, emailTemplate);
            }
        }
    }

    public void sendMarketWithdrawAlert(long withdrawAlertId) {

        BaseWithdrawAlert withdrawAlert = withdrawAlertRepository.findOne(withdrawAlertId);

        if (withdrawAlert.getWithdraw().getMarketAccount() instanceof BaseDealerMarketAccount) {
            BaseDealerMarketAccount marketAccount = (BaseDealerMarketAccount) withdrawAlert.getWithdraw().getMarketAccount();
            sendDealerMarketWithdrawAlert(withdrawAlert, marketAccount);

        } else {
            throw new ServiceException("Alerta nao implementado para esse tipo de conta virtual [%s]",
                withdrawAlert.getWithdraw().getMarketAccount().getClass());
        }

    }

    public void sendDealerMarketWithdrawAlert(BaseWithdrawAlert withdrawAlert, BaseDealerMarketAccount marketAccount) {
        BaseDealer dealer = marketAccount.getDealer();

        if (withdrawAlert.getMediaType().equals(MediaTypeEnum.EMAIL)) {

            // Descobre o template do email
            EmailTemplateConstants emailTemplate = mapMarketWithdrawEmailTemplate(withdrawAlert.getAlertType());

            if (withdrawAlert.getAlertType().getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_REQUEST.getCode())) {

                // Lista os usuarios da company que devem ser notificados
                List<BaseCompanyUser> companyUsers = companyUserRepository
                    .getByCompanyAndMarketWithdrawNotificationsTrue(dealer.getCompany());

                for (BaseCompanyUser companyUser : companyUsers) {
                    emailService.sendMarketWithdrawEmail(withdrawAlert, companyUser.getEmail(), emailTemplate);
                }

            } else if (withdrawAlert.getAlertType().getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_DENY.getCode())
                || withdrawAlert.getAlertType().getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_RELEASE.getCode())) {

                emailService.sendMarketWithdrawEmail(withdrawAlert, dealer.getEmail(), emailTemplate);
            }
        }
    }

    private EmailTemplateConstants mapInvoiceEmailTemplate(BaseAlertType alertType, BaseInvoice invoice) {

        if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET)) {

            if (alertType.getCode().equals(AlertTypeConstants.INVOICE_SEND.getCode()))
                return EmailTemplateConstants.BANK_BILLET_INVOICE_SEND;

            else if (alertType.getCode().equals(AlertTypeConstants.COLLECTION_INVOICE_EXPIRING.getCode()))
                return EmailTemplateConstants.BANK_BILLET_COLLECTION_INVOICE_EXPIRING;
            else
                throw new ServiceException("Template de email nao identificado para esse tipo de evento [%s]",
                    alertType.getCode());

        } else {
            throw new ServiceException(
                "Template de email nao identificado para essa forma de pagamento [%s] e esse tipo de evento [%s]",
                alertType.getCode(), invoice.getPaymentMethod());
        }
    }

    private EmailTemplateConstants mapMarketWithdrawEmailTemplate(BaseAlertType alertType) {

        if (alertType.getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_REQUEST.getCode()))
            return EmailTemplateConstants.MARKET_WITHDRAW_REQUEST;

        else if (alertType.getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_DENY.getCode()))
            return EmailTemplateConstants.MARKET_WITHDRAW_DENY;

        else if (alertType.getCode().equals(AlertTypeConstants.MARKET_WITHDRAW_RELEASE.getCode()))
            return EmailTemplateConstants.MARKET_WITHDRAW_RELEASE;
        else
            throw new ServiceException("Template de email nao identificado para esse tipo de evento [%s]", alertType.getCode());
    }

    @Transactional
    public void updateDeliveredAlert(BaseInvoiceAlert alert, LocalDateTime requestDate, LocalDateTime responseDate,
        BaseResultCode resultCode) {

        invoiceAlertRepository.updateStatusAndRequestDateAndResponseDateAndResultCodeById(alert.getId(),
            AlertStatusEnum.DELIVERED, requestDate, responseDate, resultCode);
    }

    @Transactional
    public void updateDeliveredAlert(BaseWithdrawAlert alert, LocalDateTime requestDate, LocalDateTime responseDate,
        BaseResultCode resultCode) {

        withdrawAlertRepository.updateStatusAndRequestDateAndResponseDateAndResultCodeById(alert.getId(),
            AlertStatusEnum.DELIVERED, requestDate, responseDate, resultCode);
    }

    public List<BaseInvoiceAlert> findDeliveredInvoiceAlerts(BaseInvoice invoice) {
        return invoiceAlertRepository.findByInvoiceAndStatus(invoice, AlertStatusEnum.DELIVERED);
    }

}
