package tech.jannotti.billing.core.connector.banking.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.connector.command.AbstractConnectorCommand;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.services.bank.BankingService;
import tech.jannotti.billing.core.services.customer.CustomerService;

public abstract class AbstractBankingCommand extends AbstractConnectorCommand {

    @Autowired
    protected BankingService bankingService;

    @Autowired
    protected CustomerService customerService;

    protected StrSubstitutor buildTemplateSubstitutor(BaseDealer dealer) {

        BaseCompany company = dealer.getCompany();
        BaseDocumentType companyDocumentType = company.getDocumentType();
        String companyDocumentNumber = companyDocumentType.format(company.getDocumentNumber());

        Map<String, String> templateProperties = new HashMap<String, String>();
        templateProperties.put("COMPANY_CORPORATE_NAME", company.getCorporateName());
        templateProperties.put("COMPANY_TRADING_NAME", company.getTradingName());
        templateProperties.put("COMPANY_DOCUMENT_TYPE", companyDocumentType.getName());
        templateProperties.put("COMPANY_DOCUMENT_NUMBER", companyDocumentNumber);

        BaseDocumentType dealerDocumentType = dealer.getDocumentType();
        String dealerDocumentNumber = dealerDocumentType.format(dealer.getDocumentNumber());

        templateProperties.put("DEALER_NAME", dealer.getName());
        templateProperties.put("DEALER_DOCUMENT_TYPE", dealerDocumentType.getName());
        templateProperties.put("DEALER_DOCUMENT_NUMBER", dealerDocumentNumber);

        StrSubstitutor substitutor = new StrSubstitutor(templateProperties);
        return substitutor;
    }

    protected String buildBeneficiaryName(BaseBank bank, StrSubstitutor templateSubstitutor) {
        return templateSubstitutor.replace(bank.getBilletBeneficiaryName());
    }

    protected String buildPaymentPlace(BaseBank bank, StrSubstitutor templateSubstitutor, boolean latePayment) {
        return templateSubstitutor.replace(bank.getBilletPaymentPlace(latePayment));
    }

    protected String buildInstructions(BaseBank bank, BaseDealer dealer, StrSubstitutor templateSubstitutor, String lineBreak,
        boolean expiredPayment) {

        String baseInstructions = templateSubstitutor.replace(bank.getBilletInstructions(expiredPayment));
        String beneficiaryInstructions = templateSubstitutor.replace(dealer.getBankBilletInstructions());

        String instructions = "";

        if (baseInstructions != null)
            instructions = baseInstructions;

        if (beneficiaryInstructions != null)
            if (!instructions.isEmpty())
                instructions += lineBreak + lineBreak + beneficiaryInstructions;
            else
                instructions = beneficiaryInstructions;

        return instructions;
    }

    protected String buildDemonstratives(BaseBank bank, StrSubstitutor templateSubstitutor) {
        return templateSubstitutor.replace(bank.getBilletDemonstratives());
    }

}
