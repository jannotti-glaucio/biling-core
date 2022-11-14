package tech.jannotti.billing.core.services.dto.request;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerServiceRequest extends AbstractServiceRequest {

    private String name;
    private PersonTypeEnum personType;
    private BaseDocumentType documentType;
    private String documentNumber;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String comments;
    private BaseCompanyBankAccount companyBankAccount;
    private BaseCompanyBillingPlan billingPlan;
    private String bankBilletInstructions;
    private boolean bankBilletExpiredPayment;
    private Integer bankBilletPenaltyPercent;
    private Integer bankBilletInterestPercent;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("name", name)
            .add("personType", personType)
            .add("documentType", documentType)
            .add("documentNumber", documentNumber)
            .add("phoneNumber", phoneNumber)
            .add("mobileNumber", mobileNumber)
            .add("email", email)
            .add("comments", comments)
            .add("companyBankAccount", companyBankAccount)
            .add("billingPlan", billingPlan)
            .add("bankBilletInstructions", bankBilletInstructions)
            .add("bankBilletExpiredPayment", bankBilletExpiredPayment)
            .add("bankBilletPenaltyPercent", bankBilletPenaltyPercent)
            .add("bankBilletInterestPercent", bankBilletInterestPercent);
    }

}
