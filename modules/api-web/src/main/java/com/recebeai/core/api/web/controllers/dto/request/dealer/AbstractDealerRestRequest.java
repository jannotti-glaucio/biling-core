package tech.jannotti.billing.core.api.web.controllers.dto.request.dealer;

import org.hibernate.validator.constraints.Email;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidBoolean;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidPersonType;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCompanyBankAccount;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCompanyBillingPlan;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDocumentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDealerRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter
    @ParameterLength(max = 100)
    private String name;

    @NotBlankParameter
    @ValidPersonType
    private String personType;

    @NotBlankParameter
    @ValidDocumentType
    private String documentType;

    @NotBlankParameter
    @ValidInteger
    @ParameterLength(max = 20)
    private String documentNumber;

    @ParameterLength(max = 15)
    private String phoneNumber;

    @ParameterLength(max = 15)
    private String mobileNumber;

    @NotBlankParameter
    @Email(message = CODE_INVALID_EMAIL_PARAMETER)
    @ParameterLength(max = 60)
    private String email;

    private String comments;

    @NotBlankParameter
    @ValidCompanyBankAccount
    private String companyBankAccount;

    @NotBlankParameter
    @ValidCompanyBillingPlan
    private String billingPlan;

    private String bankBilletInstructions;

    @NotBlankParameter
    @ValidBoolean
    private String bankBilletExpiredPayment;

    @ValidInteger
    private String bankBilletPenaltyPercent;

    @ValidInteger
    private String bankBilletInterestPercent;

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
