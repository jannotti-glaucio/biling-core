package tech.jannotti.billing.core.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.commons.util.NumberHelper;
import tech.jannotti.billing.core.connector.email.EMailConnector;
import tech.jannotti.billing.core.connector.email.EMailConnectorManager;
import tech.jannotti.billing.core.connector.response.dto.ConnectorResponseDTO;
import tech.jannotti.billing.core.constants.enums.EmailTemplateConstants;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseInvoiceAlert;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseWithdrawAlert;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.repository.base.LanguageRepository;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class EMailService extends AbstractService {

    @Value("${core.email.connector}")
    private String connectorPath;

    @Value("${core.email.from}")
    private String fromEmail;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AlertingService alertingService;

    @Autowired
    private EMailConnectorManager mailConnectorManager;

    @Transactional
    public void sendInvoiceEmail(BaseInvoiceAlert alertInvoice, byte[] attachment, EmailTemplateConstants emailTemplate) {

        BaseInvoice invoice = alertInvoice.getInvoice();
        BaseCustomer customer = invoice.getCustomer();
        BaseDealer dealer = customer.getDealer();

        BaseLanguage language = languageRepository.getByCountry(dealer.getCompany().getCountry());

        String documentNumber = customer.getDocumentType().format(customer.getDocumentNumber());
        String expirationDate = DateTimeHelper.format(invoice.getExpirationDate(), language.getDateFormat());
        String amountFormated = NumberHelper.formatAsDecimal(invoice.getAmount().intValue(), language.getNumberFormat(),
            language.getNumberDecimalSeparator());

        Map<String, String> templateProperties = new HashMap<String, String>();
        templateProperties.put("CUSTOMER_NAME", customer.getName());
        templateProperties.put("CUSTOMER_DOCUMENT_TYPE", customer.getDocumentType().getName());
        templateProperties.put("CUSTOMER_DOCUMENT_NUMBER", documentNumber);
        templateProperties.put("INVOICE_EXPIRATION_DATE", expirationDate);
        templateProperties.put("DEALER_NAME", dealer.getName());
        templateProperties.put("INVOICE_DESCRIPTION", invoice.getDescription());
        templateProperties.put("INVOICE_AMOUNT", amountFormated);

        if (invoice.getPaymentMethod().equals(PaymentMethodEnum.BANK_BILLET))
            templateProperties.put("INVOICE_LINE_CODE", invoice.getBankBillet().getLineCode());

        if (invoice.getOrderCollection() != null)
            templateProperties.put("INSTALMENT", invoice.getFullInstalment());

        String to = customer.getEmail();
        LocalDateTime requestDate = DateTimeHelper.getNowDateTime();

        EMailConnector connector = mailConnectorManager.getConnector(connectorPath);
        ConnectorResponseDTO connectorResponse = connector.sendEmail(fromEmail, to, language, emailTemplate.getName(),
            templateProperties, attachment);

        if (!connectorResponse.isSuccess())
            throw new ResultCodeServiceException(connectorResponse.getResultCode().getKey());

        LocalDateTime responseDate = DateTimeHelper.getNowDateTime();
        alertingService.updateDeliveredAlert(alertInvoice, requestDate, responseDate, connectorResponse.getResultCode());
    }

    @Transactional
    public void sendMarketWithdrawEmail(BaseWithdrawAlert withdrawAlert, String destinationMail,
        EmailTemplateConstants emailTemplate) {

        BaseMarketWithdraw withdraw = withdrawAlert.getWithdraw();
        BaseDealer dealer = withdraw.getBankAccount().getDealer();

        BaseLanguage language = languageRepository.getByCountry(dealer.getCompany().getCountry());

        String withdrawRequestDate = DateTimeHelper.format(withdraw.getRequestDate(), language.getDateFormat());
        String amountFormated = NumberHelper.formatAsDecimal(withdraw.getAmount().intValue(), language.getNumberFormat(),
            language.getNumberDecimalSeparator());

        // TODO Ver um lugar mais especifico de cada tipo de alerta pra botar isso
        Map<String, String> templateProperties = new HashMap<String, String>();
        templateProperties.put("DEALER_NAME", dealer.getName());
        templateProperties.put("WITHDRAW_REQUEST_DATE", withdrawRequestDate);
        templateProperties.put("WITHDRAW_AMOUNT", amountFormated);
        templateProperties.put("DENY_REASON", withdraw.getDenyReason());
        templateProperties.put("BANK_ACCOUNT_NAME", withdraw.getBankAccount().getDescription());

        if (withdraw.getReleaseDate() != null) {
            String withdrawReleaseDate = DateTimeHelper.format(withdraw.getReleaseDate(), language.getDateFormat());
            templateProperties.put("WITHDRAW_RELEASE_DATE", withdrawReleaseDate);
        }

        String to = destinationMail;
        LocalDateTime requestDate = DateTimeHelper.getNowDateTime();

        EMailConnector connector = mailConnectorManager.getConnector(connectorPath);
        ConnectorResponseDTO connectorResponse = connector.sendEmail(fromEmail, to, language, emailTemplate.getName(),
            templateProperties, null);

        if (!connectorResponse.isSuccess())
            throw new ResultCodeServiceException(connectorResponse.getResultCode().getKey());

        LocalDateTime responseDate = DateTimeHelper.getNowDateTime();
        alertingService.updateDeliveredAlert(withdrawAlert, requestDate, responseDate, connectorResponse.getResultCode());
    }

}
