package tech.jannotti.billing.core.api.web.controllers.dto.model.dealer;

import java.util.List;

import tech.jannotti.billing.core.api.web.controllers.dto.model.AddressDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.BankAccountDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.DocumentTypeDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.company.CompanyBillingPlanDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.company.CompanyShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerDTO extends AbstractModelDTO {

    private String token;
    private String name;
    private CompanyShortDTO company;
    private String personType;
    private DocumentTypeDTO documentType;
    private String documentNumber;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String comments;
    private BankAccountDTO companyBankAccount;
    private CompanyBillingPlanDTO billingPlan;
    private String bankBilletInstructions;
    private boolean bankBilletExpiredPayment;
    private Integer bankBilletPenaltyPercent;
    private String bankBilletInterestFrequency;
    private Integer bankBilletInterestPercent;
    private String status;

    private List<AddressDTO> addresses;
    private List<BankAccountDTO> bankAccounts;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("name", name)
            .add("company", company)
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
            .add("bankBilletInterestPercent", bankBilletInterestPercent)
            .add("status", status);
    }

}